package com.example.project_interview.controller;

import com.example.project_interview.entity.Question;
import com.example.project_interview.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService service;

    @PostMapping("/add")
    public Question addQuestion(@RequestBody Question question){
        return service.addQuestion(question);
    }

    @PostMapping("/addAtExistingForm")
    public Question addQuestionAtExistingForm(@RequestBody Question question, @PathVariable String formId){
        return service.addQuestionAtExistingForm(question,formId);
    }

    @PutMapping("/editQuestion/{id}")
    public Question editQuestion(@RequestBody Question question, @PathVariable int id){
        return service.editQuestion(question,id);
    }

    @DeleteMapping("/deleteQuestion/{id}")
    public void deleteQuestion(@PathVariable int id){
        service.deleteQuestion(id);
    }
    @GetMapping("/getQuestionsByFormId")
    public List<Question> getQuestionsByFormId(){
        return service.getQuestionsByFormId();
    }
}

