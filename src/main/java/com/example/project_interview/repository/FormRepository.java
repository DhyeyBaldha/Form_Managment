package com.example.project_interview.repository;

import com.example.project_interview.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRepository extends JpaRepository<Form, String> {
    @Query("SELECT f FROM Form f WHERE f.username = :username")
    List<Form> findAllByUser(@Param("username") String username);

}
