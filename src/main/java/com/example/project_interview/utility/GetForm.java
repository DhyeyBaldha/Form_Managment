package com.example.project_interview.utility;

import com.example.project_interview.entity.Question;
import lombok.Data;

import java.util.List;
@Data
public class GetForm {
    private String description;
    private String formId;
    private String formTitle;
    private List<Question> questions;
}
