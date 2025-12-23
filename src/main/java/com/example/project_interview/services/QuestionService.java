package com.example.project_interview.services;

import com.example.project_interview.entity.Form;
import com.example.project_interview.entity.Question;
import com.example.project_interview.repository.FormRepository;
import com.example.project_interview.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    @Autowired
    private  QuestionRepository questionRepository;

    @Autowired
    private FormRepository formRepository;

    public Question addQuestion(Question question){
        String id;
        if(formRepository == null){
            id = "FORM-1";
        }else {
            List<String> ids = formRepository.findAll().stream().map(Form::getId).toList();
            Integer maxid = ids.stream().map(s->Integer.parseInt(s.split("-")[1])).max(Integer::compare).get();
            id = "FORM-" + (maxid + 1);

        }
        question.setFormId(id);
        return questionRepository.save(question);
    }

    public Question addQuestionAtExistingForm(Question question,String formId){
        question.setFormId(formId);
        return questionRepository.save(question);
    }

    public Question editQuestion(Question question,int id){
        Question question1 = questionRepository.findById(id).get();
        question1.setLable(question.getLable());
        question1.setQuestionName(question.getQuestionName());
        question1.setAnswerType(question.getAnswerType());
        question1.setDescription(question.getDescription());
        question1.setAnswers(question.getAnswers());
        question1.setRequired(question.getRequired());
        return questionRepository.save(question);
    }

    public void deleteQuestion(int id){
        questionRepository.deleteById(id);
    }

    public List<Question> getQuestionsByFormId(){
        String id;
        if(formRepository == null){
            id = "FORM-1";
        }else {
            List<String> ids = formRepository.findAll().stream().map(Form::getId).toList();
            Integer maxid = ids.stream().map(s -> Integer.parseInt(s.split("-")[1])).max(Integer::compare).get();
            id = "FORM-" + (maxid + 1);
        }
        return questionRepository.findAllByFormId(id);
    }

}
