package com.example.project_interview.controller;

import com.example.project_interview.dto.FilledFormDto;
import com.example.project_interview.entity.FilledForm;
import com.example.project_interview.entity.User;
import com.example.project_interview.services.FilledFormServices;
import com.example.project_interview.utility.FormTitle;
import com.example.project_interview.utility.GetAuthenticatedUser;
import com.example.project_interview.mapper.FilledFormMapper;
import com.example.project_interview.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/filled-form")
@RequiredArgsConstructor
public class FilledFormController {

    @Autowired
    private FilledFormServices filledFormServices;

    @Autowired
    private GetAuthenticatedUser getAuthenticatedUser;

    @PostMapping("/save-filled-form")
    public ResponseEntity<CommonResponse<FilledFormDto>> saveFilledForm(@RequestBody FilledFormDto filledForm, HttpServletRequest request){
        FilledForm saved = filledFormServices.saveFilledForm(FilledFormMapper.toEntity(filledForm));
        FilledFormDto dto = FilledFormMapper.toDto(saved);
        return ResponseEntity.ok(
                CommonResponse.<FilledFormDto>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Filled form saved")
                        .path(request.getRequestURI())
                        .data(dto)
                        .build()
        );
    }

    @GetMapping("/completed-forms")
    public ResponseEntity<CommonResponse<List<FilledFormDto>>> completedForms(HttpServletRequest request){
        List<FilledFormDto> list = FilledFormMapper.toDtoList(filledFormServices.completedForms());
        return ResponseEntity.ok(
                CommonResponse.<List<FilledFormDto>>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Completed forms fetched")
                        .path(request.getRequestURI())
                        .data(list)
                        .build()
        );
    }

    @GetMapping("/get-form-title")
    public ResponseEntity<CommonResponse<List<FormTitle>>> getFormTitle(HttpServletRequest request){
        List<FormTitle> titles = filledFormServices.getFormTitle();
        return ResponseEntity.ok(
                CommonResponse.<List<FormTitle>>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Form titles fetched")
                        .path(request.getRequestURI())
                        .data(titles)
                        .build()
        );
    }


}
