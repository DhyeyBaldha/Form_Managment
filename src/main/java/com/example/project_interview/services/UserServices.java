package com.example.project_interview.services;

import com.example.project_interview.utility.ChangePassword;
import com.example.project_interview.entity.Role;
import com.example.project_interview.entity.User;
import com.example.project_interview.generator.UsernameGenerator;
import com.example.project_interview.repository.UserRepository;
import com.example.project_interview.utility.GetAuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class UserServices {

    @Autowired
    private UserRepository repository;

    private UsernameGenerator usernameGenerator;

    @Autowired
    private GetAuthenticatedUser getAuthenticatedUser;

    public User addUserWithImage(User user, MultipartFile file) throws IOException {
        User addUser = new User();
        addUser.setFirstname(user.getFirstname());
        addUser.setLastname(user.getLastname());
        addUser.setUsername(usernameGenerator.generateUsername(user.getFirstname(), user.getLastname()));
        if (user.getRole().equals(Role.ADMIN)) {
            addUser.setPassword("admin");
        } else if (user.getRole().equals(Role.CLIENT)) {
            addUser.setPassword("client");
        }
        addUser.setEmail(user.getEmail());
        addUser.setContactNo(user.getContactNo());
        addUser.setGender(user.getGender());
        addUser.setValidFrom(user.getValidFrom());
        addUser.setValidTo(user.getValidTo());
        addUser.setActive(true);
        addUser.setLanguage("English");
        addUser.setDescription(null);
        addUser.setProfilePicture(file.getBytes());
        return repository.save(user);
    }

    public User addUser(User user) throws IOException {
        User addUser = new User();
        addUser.setFirstname(user.getFirstname());
        addUser.setLastname(user.getLastname());
        addUser.setUsername(usernameGenerator.generateUsername(user.getFirstname(), user.getLastname()));
        if (user.getRole().equals(Role.ADMIN)) {
            addUser.setPassword("admin");
        } else if (user.getRole().equals(Role.CLIENT)) {
            addUser.setPassword("client");
        }
        addUser.setEmail(user.getEmail());
        addUser.setContactNo(user.getContactNo());
        addUser.setGender(user.getGender());
        addUser.setValidFrom(user.getValidFrom());
        addUser.setValidTo(user.getValidTo());
        addUser.setActive(true);
        addUser.setLanguage("English");
        addUser.setDescription(null);
        addUser.setProfilePicture(null);
        return repository.save(user);
    }

    public User editProfilePicture(int id, MultipartFile image) throws IOException {
        User user = repository.findById(id).orElse(null);
        user.setProfilePicture(image.getBytes());
        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public void changePassword(ChangePassword changePassword) {
        User user = repository.findById(changePassword.getId()).orElse(null);
        if (changePassword.getNewPassword() != changePassword.getConfirmPassword()) {
            throw new RuntimeException("new password and confirm password dose not match");
        } else if (user.getPassword().equals(changePassword.getOldPassword())) {
            user.setPassword(changePassword.getNewPassword());
            repository.save(user);
        } else {
            throw new RuntimeException("old password is incorrect");
        }

    }

    public User getProfile() {

        User user = getAuthenticatedUser.getAuthenticatedUser();
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;

    }

    public User editUser(int id, User user) {
        User existingUser = repository.findById(id).orElse(null);
        existingUser.setFirstname(user.getFirstname());
        existingUser.setLastname(user.getLastname());
        existingUser.setUsername(user.getUsername());
        existingUser.setPassword(user.getPassword());
        existingUser.setEmail(user.getEmail());
        existingUser.setContactNo(user.getContactNo());
        existingUser.setGender(user.getGender());
        existingUser.setValidFrom(user.getValidFrom());
        existingUser.setValidTo(user.getValidTo());
        existingUser.setActive(user.isActive());
        existingUser.setLanguage(user.getLanguage());
        existingUser.setDescription(user.getDescription());
        existingUser.setRole(user.getRole());
        return repository.save(existingUser);
    }

    public void deleteUser(int id) {
        repository.deleteById(id);
    }

    public User getUserByUsernameAndRole(String username, Role role) {
        return repository.findByUsernameAndRole(username, role);
    }

    public void addFormTOUser(String formId) {
        User user = getAuthenticatedUser.getAuthenticatedUser();
        if(user.getFormId() == null){
            user.setFormId(Collections.singletonList(formId));
        }else {
            user.getFormId().add(formId);
        }
        repository.save(user);
    }

    public void removeFormFromUser(String formId) {
        User user = getAuthenticatedUser.getAuthenticatedUser();
        user.getFormId().remove(formId);
        repository.save(user);
    }


}
