package com.example.project_interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private Instant timestamp;
    private int status;
    private boolean success;
    private String message;
    private String path;
    private T data;
}
