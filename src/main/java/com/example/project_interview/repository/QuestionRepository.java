package com.example.project_interview.repository;

import com.example.project_interview.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    @Query("SELECT q.id FROM Question q WHERE q.formId = :formId")
    List<Integer> findQuestionIdsByFormId(@Param("formId") String formId);

    List<Question> findAllByFormId(String id);
}
