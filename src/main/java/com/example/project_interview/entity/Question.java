package com.example.project_interview.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Questions")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String lable;

    private String questionName;

    private String description;

    @Enumerated(EnumType.STRING)
    private AnswerType answerType;
    private String validationType;

    @ElementCollection
    @CollectionTable(
            name = "question_answers",
            joinColumns = @JoinColumn(name = "question_id")
    )
    @Column(name = "answer")
    private List<String> answers;


    private Boolean required;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    @JsonIgnore
    private Form form;
}
