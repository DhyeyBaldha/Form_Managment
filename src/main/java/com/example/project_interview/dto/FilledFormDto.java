package com.example.project_interview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilledFormDto {
    private Long id;
    private String formId;
    private String formTitle;
    private String description;
    private String createdBy;
    private Integer userId; // reference to user
    private LocalDate completedDate;
    private List<AnsweredQuestionDto> answeredQuestions;
}
