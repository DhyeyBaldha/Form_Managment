package com.example.project_interview.dto;

import com.example.project_interview.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormDto {
    private String id;
    private String formTitle;
    private String aliasName;
    private String module;
    private String characteristic;
    private String subCharacteristic;
    private String recurrence;
    private String startMonth;
    private String compliancePeriod;
    private Date effectiveDate;
    private String text;
    private Boolean isActive;
    private List<Question> questions;
}
