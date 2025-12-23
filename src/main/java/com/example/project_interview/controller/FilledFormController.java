package com.example.project_interview.controller;

import com.example.project_interview.entity.FilledForm;
import com.example.project_interview.entity.User;
import com.example.project_interview.services.FilledFormServices;
import com.example.project_interview.utility.GetAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filled-form")
@RequiredArgsConstructor
public class FilledFormController {

    @Autowired
    private FilledFormServices filledFormServices;

    @Autowired
    private GetAuthenticatedUser getAuthenticatedUser;

    @GetMapping("/completed-forms")
    public List<FilledForm> completedForms(){
        return filledFormServices.completedForms();
    }

    @GetMapping("/get-form-title")
    public Map<String,String> getFormTitle(){
        return filledFormServices.getFormTitle();
    }

    @GetMapping("/get-all-filled-form")
    public List<FilledForm> getAllFilledForm(){
        User user = getAuthenticatedUser.getAuthenticatedUser();
        return filledFormServices.getALLFilledForm();
    }

    @GetMapping("/get-filled-form-by-id/{id}")
    public FilledForm getFilledFormById(@PathVariable int id){
         return filledFormServices.getFilledFormById(id);
    }
}
