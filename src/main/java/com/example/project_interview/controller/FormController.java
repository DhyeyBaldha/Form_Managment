package com.example.project_interview.controller;

import com.example.project_interview.dto.FormDto;
import com.example.project_interview.dto.CommonResponse;
import com.example.project_interview.utility.GetAllForm;
import com.example.project_interview.utility.GetForm;
import com.example.project_interview.services.FormService;
import com.example.project_interview.mapper.FormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/form")
@RequiredArgsConstructor
public class FormController {
    @Autowired
    private FormService formService;

    @PostMapping("/add")
    public ResponseEntity<CommonResponse<FormDto>> addForm(@RequestBody FormDto form, HttpServletRequest request){
        FormDto dto = FormMapper.toDto(formService.addForm(FormMapper.toEntity(form)));
        return ResponseEntity.ok(
                CommonResponse.<FormDto>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Form created")
                        .path(request.getRequestURI())
                        .data(dto)
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CommonResponse<GetForm>> getFormById(@PathVariable String id, HttpServletRequest request){
        GetForm gf = formService.getFormById(id);
        return ResponseEntity.ok(
                CommonResponse.<GetForm>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Form fetched")
                        .path(request.getRequestURI())
                        .data(gf)
                        .build()
        );
    }

    @GetMapping("/getAll")
    public ResponseEntity<CommonResponse<List<FormDto>>> getAllForms(HttpServletRequest request){
        List<FormDto> list = FormMapper.toDtoList(formService.getAllForms());
        return ResponseEntity.ok(
                CommonResponse.<List<FormDto>>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Forms fetched")
                        .path(request.getRequestURI())
                        .data(list)
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommonResponse<FormDto>> updateForm(@PathVariable String id, @RequestBody FormDto form, HttpServletRequest request){
        FormDto dto = FormMapper.toDto(formService.updateForm(FormMapper.toEntity(form), id));
        return ResponseEntity.ok(
                CommonResponse.<FormDto>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Form updated")
                        .path(request.getRequestURI())
                        .data(dto)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteForm(@PathVariable String id, HttpServletRequest request){
        formService.deleteForm(id);
        return ResponseEntity.ok(
                CommonResponse.<Void>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Form deleted")
                        .path(request.getRequestURI())
                        .data(null)
                        .build()
        );
    }


}
