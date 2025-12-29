package com.example.project_interview.dto;

import com.example.project_interview.entity.AnswerType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionDto {
    private Integer id;
    private String label; // maps to entity field `lable`
    private String questionName;
    private String description;
    private AnswerType answerType;
    private String validationType;
    private List<String> answers;
    private Boolean required;
    private String formId; // reference to parent Form
}
