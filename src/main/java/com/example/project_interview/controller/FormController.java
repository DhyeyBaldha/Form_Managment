package com.example.project_interview.controller;

import com.example.project_interview.entity.Form;
import com.example.project_interview.utility.GetForm;
import com.example.project_interview.services.FormService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/form")
@RequiredArgsConstructor
public class FormController {
    @Autowired
    private FormService formService;

    @PostMapping("/add")
    public Form addForm(@RequestBody Form form){
        return formService.addForm(form);
    }

    @GetMapping("/get/{id}")
    public GetForm getFormById(@PathVariable String id){
        return formService.getFormById(id);
    }

    @GetMapping("/getAll")
    public List<Form> getAllForms(){
        return formService.getAllForms();
    }

    @PutMapping("/update/{id}")
    public Form updateForm(@PathVariable String id, @RequestBody Form form){
        return formService.updateForm(form, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteForm(@PathVariable String id){
        formService.deleteForm(id);
    }
}
