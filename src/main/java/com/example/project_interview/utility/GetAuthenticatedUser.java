package com.example.project_interview.utility;

import com.example.project_interview.entity.User;
import com.example.project_interview.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class GetAuthenticatedUser {
    @Autowired
    private UserRepository repository;

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return repository.findByUsername(username);
        }
        throw new RuntimeException("User is not authenticated");
    }
}
