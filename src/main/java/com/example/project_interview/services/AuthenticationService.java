package com.example.project_interview.services;



import com.example.project_interview.auth.AuthenticationResponse;
import com.example.project_interview.dto.LoginDto;
import com.example.project_interview.entity.Role;
import com.example.project_interview.entity.User;
import com.example.project_interview.repository.UserRepository;
import com.example.project_interview.Exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.NoSuchElementException;



@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User request){
        try {
            User user=new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.valueOf("ADMIN"));
            user = repository.save(user);
            String token = jwtService.generateToken(user);
            return new AuthenticationResponse(token);
        } catch (DataIntegrityViolationException e) {
            log.warn("Registration failed, email already exists: {}", request.getEmail());
            throw new UserAlreadyExistsException("User with this email already exists");
        } catch (Exception e) {
            log.error("Unexpected error during registration for email {}", request.getEmail(), e);
            throw e;
        }
    }
    public AuthenticationResponse authenticate(LoginDto request){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            log.warn("Authentication failed for email {}: {}", request.getEmail(), ex.getMessage());
            throw ex;
        }

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        String token = jwtService.generateToken(user);

        return new AuthenticationResponse(token);

    }
}
