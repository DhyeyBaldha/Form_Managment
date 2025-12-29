package com.example.project_interview.mapper;

import com.example.project_interview.dto.AnsweredQuestionDto;
import com.example.project_interview.entity.AnsweredQuestion;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Data
public final class AnsweredQuestionMapper {
    private AnsweredQuestionMapper() {}

    public static AnsweredQuestionDto toDto(AnsweredQuestion a) {
        if (a == null) return null;
        AnsweredQuestionDto dto = new AnsweredQuestionDto();
        dto.setId(a.getId());
        dto.setQuestionId(a.getQuestionId());
        dto.setQuestionName(a.getQuestionName());
        dto.setDescription(a.getDescription());
        dto.setAnswerRequired(a.isAnswerRequired());
        dto.setAnswers(a.getAnswers());
        return dto;
    }

    public static List<AnsweredQuestionDto> toDtoList(List<AnsweredQuestion> list) {
        if (list == null) return null;
        return list.stream().filter(Objects::nonNull).map(AnsweredQuestionMapper::toDto).collect(Collectors.toList());
    }

    public static AnsweredQuestion toEntity(AnsweredQuestionDto dto) {
        if (dto == null) return null;
        AnsweredQuestion a = new AnsweredQuestion();
        a.setId(dto.getId());
        a.setQuestionId(dto.getQuestionId());
        a.setQuestionName(dto.getQuestionName());
        a.setDescription(dto.getDescription());
        a.setAnswerRequired(dto.isAnswerRequired());
        a.setAnswers(dto.getAnswers());
        return a;
    }
}
