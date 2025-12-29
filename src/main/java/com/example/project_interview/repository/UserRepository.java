package com.example.project_interview.repository;

import com.example.project_interview.entity.IsActive;
import com.example.project_interview.entity.Role;
import com.example.project_interview.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.isActive <> :status")
    List<User> findAllNotDeleted(@Param("status") IsActive status);



    @Query(
            "SELECT u FROM User u " +
                    "WHERE u.isActive <> :deleted " +
                    "AND (:username IS NULL OR u.username = :username) " +
                    "AND (:role IS NULL OR u.role = :role)"
    )
    List<User> findUsers(
            @Param("username") String username,
            @Param("role") Role role,
            @Param("deleted") IsActive deleted
    );



    List<User> findAllByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findAllByRole(@Param("role")Role username);
}
