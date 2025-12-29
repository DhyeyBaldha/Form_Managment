package com.example.project_interview.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnsweredQuestionDto {
    private Long id;
    private Integer questionId;
    private String questionName;
    private String description;
    private boolean answerRequired;
    private List<String> answers;
}
