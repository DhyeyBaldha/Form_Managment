package com.example.project_interview.services;

import com.example.project_interview.entity.Form;
import com.example.project_interview.entity.Question;
import com.example.project_interview.entity.User;
import com.example.project_interview.utility.GetAllForm;
import com.example.project_interview.utility.GetAuthenticatedUser;
import com.example.project_interview.utility.GetForm;
import com.example.project_interview.generator.IdGenerator;
import com.example.project_interview.repository.FormRepository;
import com.example.project_interview.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public Form addForm(Form form) {
        try {
            String id = idGenerator.generateFormId(repository.findAll().stream().map(Form::getId).toList());
            form.setId(id);
            User user = getAuthenticatedUser.getAuthenticatedUser();
            if (user == null) {
                throw new NoSuchElementException("Authenticated user not found");
            }
            form.setUser(user);
            if (form.getQuestions() != null) {
                for (Question question : form.getQuestions()) {
                    question.setForm(form);
                }
            }
            validateForm(form);
            return repository.save(form);
        } catch (DataAccessException e) {
            log.error("Failed to save form: {}", form.getFormTitle(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while adding form", e);
            throw e;
        }

    }

    public GetForm getFormById(String id) {
        try {
            Form form = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Form not found"));
            GetForm getForm = new GetForm();
            getForm.setFormTitle(form.getFormTitle());
            getForm.setFormId(form.getId());
            getForm.setDescription(form.getText());
            getForm.setQuestions(form.getQuestions());
            return getForm;
        } catch (NoSuchElementException e) {
            log.warn("Form not found for id: {}", id);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while fetching form by id {}", id, e);
            throw e;
        }
    }

    public List<Form> getAllForms() {
        try {
            User user = getAuthenticatedUser.getAuthenticatedUser();
            if (user == null) {
                throw new NoSuchElementException("Authenticated user not found");
            }
            return repository.findAllByUser(user);
        } catch (DataAccessException e) {
            log.error("Failed to fetch all forms for current user", e);
            throw e;
        }
    }


    public Form updateForm(Form form, String id) {
        try {
            User user = getAuthenticatedUser.getAuthenticatedUser();
            if (user == null) {
                throw new NoSuchElementException("Authenticated user not found");
            }
            Form form1 = repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Form not found"));
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
            if (form.getQuestions() != null) {

                form1.getQuestions().clear();

                for (Question q : form.getQuestions()) {
                    q.setForm(form1);
                    form1.getQuestions().add(q);
                }
            }
            form1.setUser(user);
            validateForm(form1);
            return repository.save(form1);
        } catch (NoSuchElementException e) {
            log.warn("Update failed. Form not found: {}", id);
            throw e;
        } catch (DataAccessException e) {
            log.error("Failed to update form {}", id, e);
            throw e;
        }
    }

    public void deleteForm(String id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Delete failed. Form not found: {}", id);
            throw new NoSuchElementException("Form not found");
        } catch (DataAccessException e) {
            log.error("Failed to delete form {}", id, e);
            throw e;
        }
    }


    // Validation helpers
    private void validateForm(Form form) {
        if (form == null) {
            throw new IllegalArgumentException("Form must not be null");
        }
        if (form.getFormTitle() == null || form.getFormTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Form title is required");
        }
        if (form.getQuestions() != null) {
            for (Question q : form.getQuestions()) {
                validateQuestion(q);
            }
        }
    }

    private void validateQuestion(Question q) {
        if (q == null) {
            throw new IllegalArgumentException("Question must not be null");
        }
        if (q.getQuestionName() == null || q.getQuestionName().trim().isEmpty()) {
            throw new IllegalArgumentException("Question name is required");
        }
        if (q.getAnswerType() == null) {
            throw new IllegalArgumentException("Answer type is required for question: " + q.getQuestionName());
        }
        switch (q.getAnswerType()) {
            case SingleChoice, MultipleChoice, SingleSelectDropdown, MultiSelectDropdown -> {
                if (q.getAnswers() == null || q.getAnswers().isEmpty()) {
                    throw new IllegalArgumentException("Options are required for question: " + q.getQuestionName());
                }
            }
            case NoAnswerRequired -> {
                // No options needed
            }
            case SingleTextbox, MultilineTextbox, Date -> {
                // No predefined options required
            }
        }
    }
}
