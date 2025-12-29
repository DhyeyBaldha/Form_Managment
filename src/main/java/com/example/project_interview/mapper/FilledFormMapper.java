package com.example.project_interview.mapper;

import com.example.project_interview.dto.FilledFormDto;
import com.example.project_interview.entity.FilledForm;
import com.example.project_interview.entity.User;
import com.example.project_interview.entity.AnsweredQuestion;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Data
public final class FilledFormMapper {
    private FilledFormMapper() {}

    public static FilledFormDto toDto(FilledForm f) {
        if (f == null) return null;
        FilledFormDto dto = new FilledFormDto();
        dto.setId(f.getId());
        dto.setFormId(f.getFormId());
        dto.setFormTitle(f.getFormTitle());
        dto.setDescription(f.getDescription());
        dto.setCreatedBy(f.getCreatedBy());
        dto.setUserId(f.getUser() != null ? f.getUser().getId() : null);
        dto.setCompletedDate(f.getCompletedDate());
        dto.setAnsweredQuestions(AnsweredQuestionMapper.toDtoList(f.getAnsweredQuestions()));
        return dto;
    }

    public static List<FilledFormDto> toDtoList(List<FilledForm> list) {
        if (list == null) return null;
        return list.stream().filter(Objects::nonNull).map(FilledFormMapper::toDto).collect(Collectors.toList());
    }

    public static FilledForm toEntity(FilledFormDto dto) {
        if (dto == null) return null;
        FilledForm f = new FilledForm();
        f.setId(dto.getId());
        f.setFormId(dto.getFormId());
        f.setFormTitle(dto.getFormTitle());
        f.setDescription(dto.getDescription());
        f.setCreatedBy(dto.getCreatedBy());
        f.setCompletedDate(dto.getCompletedDate());
        if (dto.getAnsweredQuestions() != null) {
            f.setAnsweredQuestions(
                    dto.getAnsweredQuestions().stream()
                            .filter(Objects::nonNull)
                            .map(aDto -> {
                                AnsweredQuestion a = new AnsweredQuestion();
                                a.setId(aDto.getId());
                                a.setQuestionId(aDto.getQuestionId());
                                a.setQuestionName(aDto.getQuestionName());
                                a.setDescription(aDto.getDescription());
                                a.setAnswers(aDto.getAnswers());
                                return a;
                            })
                            .collect(Collectors.toList())
            );
        }
        return f;
    }

    public static FilledForm toEntityWithUser(FilledFormDto dto, User user) {
        FilledForm f = toEntity(dto);
        if (f != null) {
            f.setUser(user);
        }
        return f;
    }
}
