package com.example.project_interview.services;

import com.example.project_interview.entity.Form;
import com.example.project_interview.entity.Question;
import com.example.project_interview.entity.User;
import com.example.project_interview.utility.GetAuthenticatedUser;
import com.example.project_interview.utility.GetForm;
import com.example.project_interview.generator.IdGenerator;
import com.example.project_interview.repository.FormRepository;
import com.example.project_interview.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormService {

    @Autowired
    private FormRepository repository;

    @Autowired
    private UserServices userServices;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private GetAuthenticatedUser getAuthenticatedUser;

    public Form addForm(Form form){
        String id = idGenerator.generateFormId(repository.findAll().stream().map(Form::getId).toList());
        form.setId(id);
        List<Integer> ids = questionRepository.findQuestionIdsByFormId(id);
        form.setQuestions(ids);
        User user = getAuthenticatedUser.getAuthenticatedUser();
        form.setUsername(user.getUsername());
        userServices.addFormTOUser(id);
        return repository.save(form);

    }

    public GetForm getFormById(String id){
        Form form = repository.findById(id).get();
        List<Question> question = questionRepository.findAllById(form.getQuestions());
        GetForm getForm = new GetForm();
        getForm.setForm(form);
        getForm.setQuestions(question);
        return getForm;
    }

    public List<Form> getAllForms(){
        User user = getAuthenticatedUser.getAuthenticatedUser();
        return repository.findAllByUser(user.getUsername());
    }

    public Form updateForm(Form form,String id){
        Form form1 = repository.findById(id).get();
        form1.setFormTitle(form.getFormTitle());
        form1.setAliasName(form.getAliasName());
        form1.setModule(form.getModule());
        form1.setCharacteristic(form.getCharacteristic());
        form1.setSubCharacteristic(form.getSubCharacteristic());
        form1.setRecurrence(form.getRecurrence());
        form1.setStartMonth(form.getStartMonth());
        form1.setCompliancePeriod(form.getCompliancePeriod());
        form1.setEffectiveDate(form.getEffectiveDate());
        form1.setText(form.getText());
        form1.setIsActive(form.getIsActive());
        List<Integer> ids = questionRepository.findQuestionIdsByFormId(id);
        form.setQuestions(ids);
        return repository.save(form1);
    }

    public void deleteForm(String id){
        repository.deleteById(id);
        userServices.removeFormFromUser(id);
    }


}
