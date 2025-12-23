package com.example.project_interview.utility;

import com.example.project_interview.entity.Form;
import com.example.project_interview.entity.Question;
import lombok.Data;

import java.util.List;
@Data
public class GetForm {
    private Form form;
    private List<Question> questions;
}
