package com.example.project_interview.services;

import com.cloudinary.Cloudinary;
import com.example.project_interview.entity.IsActive;
import com.example.project_interview.utility.ChangePassword;
import com.example.project_interview.entity.Role;
import com.example.project_interview.entity.User;
import com.example.project_interview.repository.UserRepository;
import com.example.project_interview.utility.GetAuthenticatedUser;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import com.example.project_interview.Exception.UserAlreadyExistsException;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServices {
    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private GetAuthenticatedUser getAuthenticatedUser;

    @Autowired
    private EmailService emailService;

public User addUserWithImage(User user, MultipartFile file) throws IOException {

    User addUser = new User();

    if (user.getFirstname() == null || user.getFirstname().isBlank()) {
        throw new IllegalArgumentException("First name is required");
    }
    if (user.getLastname() == null || user.getLastname().isBlank()) {
        throw new IllegalArgumentException("Last name is required");
    }
    if (user.getEmail() == null || user.getEmail().isBlank()) {
        throw new IllegalArgumentException("Email is required");
    }

    String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
    if (!Pattern.compile(emailRegex).matcher(user.getEmail()).matches()) {
        throw new IllegalArgumentException("Invalid email format");
    }
    if (user.getRole() == null) {
        throw new IllegalArgumentException("Role is required");
    }
    if (user.getValidFrom() != null && user.getValidTo() != null && user.getValidFrom().isAfter(user.getValidTo())) {
        throw new IllegalArgumentException("validFrom cannot be after validTo");
    }
    repository.findByEmail(user.getEmail()).ifPresent(u -> { throw new UserAlreadyExistsException("User with this email already exists"); });

    addUser.setFirstname(user.getFirstname());
    addUser.setLastname(user.getLastname());
    addUser.setUsername(user.getFirstname()+" "+user.getLastname());

    addUser.setPassword(passwordEncoder.encode(user.getFirstname().toLowerCase()+user.getLastname().toLowerCase()));

    addUser.setEmail(user.getEmail());
    addUser.setContactNo(user.getContactNo());
    addUser.setGender(user.getGender());
    addUser.setValidFrom(user.getValidFrom());
    addUser.setValidTo(user.getValidTo());
    addUser.setRole(user.getRole());

    addUser.setLanguage(user.getLanguage());
    addUser.setIsActive(IsActive.TRUE);
    addUser.setDescription(null);

    try {
        if (file != null && !file.isEmpty()) {
            if (file.getContentType() == null || !file.getContentType().toLowerCase().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed for profile picture");
            }
            final Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            addUser.setProfilePicture(data.get("url").toString());
        }
        try {
            sendVerificationEmail(user.getEmail(), addUser);
        } catch (MessagingException e) {
            log.warn("Failed to send verification email to {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }

        return repository.save(addUser);
    } catch (DataAccessException e) {
        log.error("Failed to save user {} {}", user.getFirstname(), user.getLastname(), e);
        throw e;
    }
}


    public User editProfilePicture( MultipartFile image) throws IOException {
        try {
            User user = getAuthenticatedUser.getAuthenticatedUser();
            if (user == null) {
                throw new NoSuchElementException("Authenticated user not found");
            }
            if (image == null || image.isEmpty()) {
                throw new IllegalArgumentException("Image file is required");
            }
            if (image.getContentType() == null || !image.getContentType().toLowerCase().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed for profile picture");
            }
            final Map data = this.cloudinary.uploader().upload(image.getBytes(), Map.of());
            user.setProfilePicture(data.get("url").toString());
            return repository.save(user);
        } catch (DataAccessException e) {
            log.error("Failed to update profile picture", e);
            throw e;
        }
    }

    public List<User> getAllUsers() {
        try {
            return repository.findAllNotDeleted(IsActive.DELETED);
        } catch (DataAccessException e) {
            log.error("Failed to fetch all users", e);
            throw e;
        }
    }

    public String changePassword(ChangePassword changePassword) {

        User user = getAuthenticatedUser.getAuthenticatedUser();
        if (changePassword.getOldPassword() == null || changePassword.getOldPassword().isBlank()) {
            throw new IllegalArgumentException("Old password is required");
        }
        if (changePassword.getNewPassword() == null || changePassword.getNewPassword().isBlank()) {
            throw new IllegalArgumentException("New password is required");
        }
        if (changePassword.getConfirmPassword() == null || changePassword.getConfirmPassword().isBlank()) {
            throw new IllegalArgumentException("Confirm password is required");
        }
        if (changePassword.getNewPassword().length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters long");
        }
        if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        if (!passwordEncoder.matches(
                changePassword.getOldPassword(),
                user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        if (passwordEncoder.matches(
                changePassword.getNewPassword(),
                user.getPassword())) {
            throw new RuntimeException("New password must be different from old password");
        }

        user.setPassword(
                passwordEncoder.encode(changePassword.getNewPassword())
        );

        try {
            repository.save(user);
        } catch (DataAccessException e) {
            log.error("Failed to update password for user {}", user.getEmail(), e);
            throw e;
        }
        return "Password changed successfully";
    }


    public User getProfile() {

        User user = getAuthenticatedUser.getAuthenticatedUser();
        if (user == null) {
            log.warn("Profile requested but user not found in context");
            throw new RuntimeException("User not found");
        }
        return user;

    }

    public User editUser(int id, User user, MultipartFile file,boolean removeProfilePicture) throws IOException {
        try {
            User existingUser = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            existingUser.setFirstname(user.getFirstname());
            existingUser.setLastname(user.getLastname());
            existingUser.setPassword(user.getPassword());
            existingUser.setContactNo(user.getContactNo());
            existingUser.setGender(user.getGender());
            existingUser.setValidFrom(user.getValidFrom());
            existingUser.setValidTo(user.getValidTo());
            if (existingUser.getValidFrom() != null && existingUser.getValidTo() != null && existingUser.getValidFrom().isAfter(existingUser.getValidTo())) {
                throw new IllegalArgumentException("validFrom cannot be after validTo");
            }
            existingUser.setIsActive(user.getIsActive());
            existingUser.setLanguage(user.getLanguage());
            existingUser.setDescription(user.getDescription());
            existingUser.setRole(user.getRole());
            if (Boolean.TRUE.equals(removeProfilePicture)) {
                existingUser.setProfilePicture(null);

            }else if (file != null && !file.isEmpty()) {
                final Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
                existingUser.setProfilePicture(data.get("url").toString());
            }
            return repository.save(existingUser);
        } catch (DataAccessException e) {
            log.error("Failed to edit user {}", id, e);
            throw e;
        }
    }

    public void deleteUser(int id) {

            User user = repository.findById(id).get();
            user.setIsActive(IsActive.DELETED);
            repository.save(user);
    }

    public List<User> getUserByUsernameAndRole(String username, String role) {


        if (username != null && username.isBlank()) {
        username = null;
    }
        Role roleEnum;
        try {
            roleEnum = (role == null || role.isBlank())
                ? null
                : Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
        try {
            return repository.findUsers(username, roleEnum,IsActive.DELETED);
        } catch (DataAccessException e) {
            log.error("Failed to find users by username/role", e);
            throw e;
        }
    }
    public void sendVerificationEmail(String email, User user) throws MessagingException {
        String subject = "Email Verification";
        String text =
                "Your account has been successfully created.\n\n" +
                        "Login Email: " + user.getEmail() + "\n" +
                        "Temporary Password: "+ "["+ user.getFirstname().toLowerCase()+user.getLastname().toLowerCase() +"]" + "\n\n" +
                        "Please log in and change your password immediately after your first login.\n\n";

        emailService.sendEmail(email, subject, text);
    }

}
