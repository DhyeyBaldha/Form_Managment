package com.example.project_interview.utility;

import com.example.project_interview.entity.User;
import com.example.project_interview.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class GetAuthenticatedUser {
    @Autowired
    private UserRepository repository;

    public User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        String email = authentication.getName();

        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Authenticated user not found in database"));
    }

}
