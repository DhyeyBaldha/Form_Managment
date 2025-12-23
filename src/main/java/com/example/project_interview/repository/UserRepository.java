package com.example.project_interview.repository;

import com.example.project_interview.entity.Role;
import com.example.project_interview.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
    User findByUsernameAndRole(String username, Role role);
    Boolean existsByUsername(String username);
}
