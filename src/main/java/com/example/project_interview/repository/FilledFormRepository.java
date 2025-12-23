package com.example.project_interview.repository;

import com.example.project_interview.entity.FilledForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilledFormRepository extends JpaRepository<FilledForm, Integer> {
    List<FilledForm> findAllByUserId(Integer id);
    // Add custom query methods here if needed
}
