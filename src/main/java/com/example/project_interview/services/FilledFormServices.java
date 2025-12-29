package com.example.project_interview.services;

import com.example.project_interview.entity.*;
import com.example.project_interview.repository.FilledFormRepository;
import com.example.project_interview.repository.FormRepository;
import com.example.project_interview.repository.QuestionRepository;
import com.example.project_interview.utility.FormTitle;
import com.example.project_interview.utility.GetAuthenticatedUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Set;
import java.time.format.DateTimeParseException;
@RequiredArgsConstructor
@Service
@Slf4j
public class FilledFormServices {

    @Autowired
    private FilledFormRepository filledFormRepository;
    @Autowired
    private GetAuthenticatedUser getAuthenticatedUser;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public FilledForm saveFilledForm(FilledForm filledForm) {
        try {
            User user = getAuthenticatedUser.getAuthenticatedUser();
            if (user == null) {
                throw new java.util.NoSuchElementException("Authenticated user not found");
            }
            Form form = formRepository.findById(filledForm.getFormId())
                    .orElseThrow(() -> new java.util.NoSuchElementException("Form not found"));
            List<FilledForm> filledForms = filledFormRepository.findAllByUserId(user.getId());
            if (filledForms.stream().anyMatch(ff -> ff.getFormId().equals(filledForm.getFormId()))) {
                throw new IllegalArgumentException("Form already filled");
            }
            filledForm.setFormTitle(form.getFormTitle());
            filledForm.setDescription(form.getText());
            filledForm.setCompletedDate(LocalDate.now());
            filledForm.setUser(user);
            filledForm.setCreatedBy(form.getUser().getDbUsername());
            if (filledForm.getAnsweredQuestions() == null) {
                filledForm.setAnsweredQuestions(new ArrayList<>());
            } else {
                filledForm.setAnsweredQuestions(
                        filledForm.getAnsweredQuestions()
                                .stream()
                                .map(answeredQuestion -> {
                                    AnsweredQuestion aq = new AnsweredQuestion();
                                    Question question = questionRepository.findById(answeredQuestion.getQuestionId())
                                            .orElseThrow(() -> new java.util.NoSuchElementException("Question not found"));
                                    aq.setQuestionId(answeredQuestion.getQuestionId());
                                    aq.setAnswers(answeredQuestion.getAnswers());
                                    aq.setAnswerRequired(question.getRequired());
                                    aq.setQuestionName(answeredQuestion.getQuestionName());
                                    aq.setDescription(answeredQuestion.getDescription());
                                    aq.setFilledForm(filledForm);
                                    return aq;
                                })
                                .toList()
                );
            }

            // Validate answers against question definitions
            validateFilledFormAnswers(filledForm, form);

            return filledFormRepository.save(filledForm);
        } catch (DataAccessException e) {
            log.error("Failed to save filled form for formId {}", filledForm.getFormId(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving filled form", e);
            throw e;
        }
    }


    public List<FilledForm> completedForms(){
        try {
            User user = getAuthenticatedUser.getAuthenticatedUser();
            if (user == null) {
                throw new java.util.NoSuchElementException("Authenticated user not found");
            }
            return filledFormRepository.findAllByUserId(user.getId());
        } catch (DataAccessException e) {
            log.error("Failed to fetch completed forms for current user", e);
            throw e;
        }
    }

    public List<FormTitle> getFormTitle() {
        try {
            User user = getAuthenticatedUser.getAuthenticatedUser();
            if (user == null) {
                throw new java.util.NoSuchElementException("Authenticated user not found");
            }
            Integer userId = user.getId();

            List<String> filledFormIds =
                    filledFormRepository.findAllByUserId(userId)
                            .stream()
                            .map(FilledForm::getFormId)
                            .toList();

            return formRepository.findAll()
                    .stream()
                    .filter(form -> !filledFormIds.contains(form.getId()))
                    .map(form -> new FormTitle(form.getFormTitle(), form.getId()))
                    .collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Failed to get form titles", e);
            throw e;
        }
    }

    // Validation helpers for filled forms
    private void validateFilledFormAnswers(FilledForm filledForm, Form form) {
        List<AnsweredQuestion> answers = filledForm.getAnsweredQuestions();
        if (answers == null) {
            answers = new ArrayList<>();
        }
        // Build question map
        Map<Integer, Question> questionMap = new HashMap<>();
        if (form.getQuestions() != null) {
            for (Question q : form.getQuestions()) {
                questionMap.put(q.getId(), q);
            }
        }

        // Validate each answered question
        for (AnsweredQuestion aq : answers) {
            if (aq.getQuestionId() == null) {
                throw new IllegalArgumentException("Answered question missing questionId");
            }
            Question q = questionMap.get(aq.getQuestionId());
            if (q == null) {
                // fallback to repository if not in map
                q = questionRepository.findById(aq.getQuestionId())
                        .orElseThrow(() -> new java.util.NoSuchElementException("Question not found"));
                questionMap.put(q.getId(), q);
            }

            // Required enforcement
            if (Boolean.TRUE.equals(q.getRequired())) {
                if (aq.getAnswers() == null || aq.getAnswers().isEmpty() ||
                        aq.getAnswers().stream().allMatch(s -> s == null || s.trim().isEmpty())) {
                    throw new IllegalArgumentException("Answer required for question: " + q.getQuestionName());
                }
            }

            // Answer type validation
            switch (q.getAnswerType()) {
                case SingleChoice, SingleSelectDropdown -> {
                    if (aq.getAnswers() == null || aq.getAnswers().size() != 1) {
                        throw new IllegalArgumentException("Exactly one answer required for question: " + q.getQuestionName());
                    }
                    String val = aq.getAnswers().get(0);
                    if (q.getAnswers() == null || !q.getAnswers().contains(val)) {
                        throw new IllegalArgumentException("Invalid option for question: " + q.getQuestionName());
                    }
                }
                case MultipleChoice, MultiSelectDropdown -> {
                    if (aq.getAnswers() == null || aq.getAnswers().isEmpty()) {
                        throw new IllegalArgumentException("At least one answer required for question: " + q.getQuestionName());
                    }
                    if (q.getAnswers() != null) {
                        for (String val : aq.getAnswers()) {
                            if (!q.getAnswers().contains(val)) {
                                throw new IllegalArgumentException("Invalid option for question: " + q.getQuestionName());
                            }
                        }
                    }
                }
                case SingleTextbox, MultilineTextbox -> {
                    if (aq.getAnswers() == null || aq.getAnswers().isEmpty() || aq.getAnswers().get(0).isBlank()) {
                        if (Boolean.TRUE.equals(q.getRequired())) {
                            throw new IllegalArgumentException("Text answer required for question: " + q.getQuestionName());
                        }
                    }
                }
                case Date -> {
                    if (aq.getAnswers() == null || aq.getAnswers().isEmpty()) {
                        if (Boolean.TRUE.equals(q.getRequired())) {
                            throw new IllegalArgumentException("Date answer required for question: " + q.getQuestionName());
                        }
                    } else {
                        String dateStr = aq.getAnswers().get(0);
                        try {
                            java.time.LocalDate.parse(dateStr);
                        } catch (DateTimeParseException ex) {
                            throw new IllegalArgumentException("Invalid date format for question: " + q.getQuestionName());
                        }
                    }
                }
                case NoAnswerRequired -> {
                    // no validation
                }
            }
        }

        // Ensure all required questions are answered
        if (form.getQuestions() != null) {
            Set<Integer> answeredIds = answers.stream()
                    .map(AnsweredQuestion::getQuestionId)
                    .collect(java.util.stream.Collectors.toSet());
            for (Question q : form.getQuestions()) {
                if (Boolean.TRUE.equals(q.getRequired()) && !answeredIds.contains(q.getId())) {
                    throw new IllegalArgumentException("Missing required question: " + q.getQuestionName());
                }
            }
        }
    }

}
