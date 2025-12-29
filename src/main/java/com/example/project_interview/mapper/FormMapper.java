package com.example.project_interview.mapper;

import com.example.project_interview.dto.FormDto;
import com.example.project_interview.entity.Form;
import lombok.Data;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
@Data
public final class FormMapper {
    private FormMapper() {}

    public static FormDto toDto(Form form) {
        if (form == null) return null;
        FormDto dto = new FormDto();
        dto.setId(form.getId());
        dto.setFormTitle(form.getFormTitle());
        dto.setAliasName(form.getAliasName());
        dto.setModule(form.getModule());
        dto.setCharacteristic(form.getCharacteristic());
        dto.setSubCharacteristic(form.getSubCharacteristic());
        dto.setRecurrence(form.getRecurrence());
        dto.setStartMonth(form.getStartMonth());
        dto.setCompliancePeriod(form.getCompliancePeriod());
        dto.setEffectiveDate(form.getEffectiveDate());
        dto.setText(form.getText());
        dto.setIsActive(form.getIsActive());
        dto.setQuestions(form.getQuestions());
        return dto;
    }

    public static List<FormDto> toDtoList(List<Form> list) {
        if (list == null) return null;
        return list.stream().filter(Objects::nonNull).map(FormMapper::toDto).collect(Collectors.toList());
    }

    public static Form toEntity(FormDto dto) {
        if (dto == null) return null;
        Form form = new Form();
        form.setId(dto.getId());
        form.setFormTitle(dto.getFormTitle());
        form.setAliasName(dto.getAliasName());
        form.setModule(dto.getModule());
        form.setCharacteristic(dto.getCharacteristic());
        form.setSubCharacteristic(dto.getSubCharacteristic());
        form.setRecurrence(dto.getRecurrence());
        form.setStartMonth(dto.getStartMonth());
        form.setCompliancePeriod(dto.getCompliancePeriod());
        form.setEffectiveDate(dto.getEffectiveDate());
        form.setText(dto.getText());
        form.setIsActive(dto.getIsActive());
        if (form != null) {
            form.setQuestions(dto.getQuestions());
        } 
        // questions should be attached with form reference; use toEntityWithQuestions if needed
        return form;
    }

    public static Form toEntityWithQuestions(FormDto dto) {
        Form form = toEntity(dto);
        if (form != null) {
            form.setQuestions(dto.getQuestions());
        }
        return form;
    }
}
