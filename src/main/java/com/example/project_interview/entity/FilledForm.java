package com.example.project_interview.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "filled_form")
@Data
public class FilledForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String formId;
    private String formTitle;
    private String description;
    private String createdBy;

    private Integer userId;
    private LocalDate completedDate;

    @OneToMany(
            mappedBy = "filledForm",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<AnsweredQuestion> answeredQuestions;
}

