package com.example.project_interview.services;

import com.example.project_interview.entity.FilledForm;
import com.example.project_interview.entity.Form;
import com.example.project_interview.entity.User;
import com.example.project_interview.repository.FilledFormRepository;
import com.example.project_interview.repository.FormRepository;
import com.example.project_interview.utility.GetAuthenticatedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilledFormServices {
    @Autowired
    private FilledFormRepository filledFormRepository;
    @Autowired
    private GetAuthenticatedUser getAuthenticatedUser;

    @Autowired
    private FormRepository formRepository;


    public FilledForm saveFilledForm(FilledForm filledForm, String formId) {
        User user = getAuthenticatedUser.getAuthenticatedUser();
        filledForm.setCompletedDate(LocalDate.now());
        filledForm.setUserId(user.getId());
        filledForm.setFormId(formId);
        return filledFormRepository.save(filledForm);
    }


    public List<FilledForm> completedForms(){
        User user = getAuthenticatedUser.getAuthenticatedUser();
        return filledFormRepository.findAllByUserId(user.getId());
    }

    public Map<String,String> getFormTitle() {

        User user = getAuthenticatedUser.getAuthenticatedUser();
        Integer userId = user.getId();

        List<String> filledFormIds =
                filledFormRepository.findAllByUserId(userId)
                        .stream()
                        .map(FilledForm::getFormId)
                        .toList();

        return formRepository.findAll()
                .stream()
                .filter(form -> !filledFormIds.contains(form.getId()))
                .collect(Collectors.toMap(Form::getId, Form::getFormTitle));
    }

    public List<FilledForm> getALLFilledForm(){
        return filledFormRepository.findAll();
    }

    public FilledForm getFilledFormById(int id){
        return filledFormRepository.findById(id).get();
    }

}
