package com.example.project_interview.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Data;

import java.util.List;

@Entity
@Table(name = "answered_question")
@Data
public class AnsweredQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer questionId;
    private String questionName;
    private String description;
    private boolean answerRequired;

    @ElementCollection
    @CollectionTable(
            name = "answered_question_values",
            joinColumns = @JoinColumn(name = "answered_question_id")
    )
    @Column(name = "answer_value")
    private List<String> answers;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "filled_form_id")
    private FilledForm filledForm;
}
