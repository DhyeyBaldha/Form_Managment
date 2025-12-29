package com.example.project_interview.repository;

import com.example.project_interview.entity.Form;
import com.example.project_interview.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {
    @Query("SELECT f FROM Form f WHERE f.user = :user")
    List<Form> findAllByUser(@Param("user") User user);
    @Query(" SELECT f FROM Form f WHERE LOWER(f.formTitle) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Form> findAllByFormTitle(String title);
}
