package com.example.project_interview.Exception;

import com.example.project_interview.dto.CommonResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.Instant;
import org.springframework.security.core.AuthenticationException;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<CommonResponse<Object>> handleUserAlreadyExistsException(UserAlreadyExistsException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        log.warn("User already exists: {}", ex.getMessage());
        CommonResponse<Object> body = CommonResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .success(false)
                .message("User already exists")
                .path(request.getRequestURI())
                .data(null)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<Object>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        log.warn("Authentication failed: {}", ex.getMessage());
        CommonResponse<Object> body = CommonResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .success(false)
                .message("Authentication failed")
                .path(request.getRequestURI())
                .data(null)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn("Bad request: {}", ex.getMessage());
        CommonResponse<Object> body = CommonResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .success(false)
                .message("Invalid request")
                .path(request.getRequestURI())
                .data(null)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonResponse<Object>> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.warn("Resource not found: {}", ex.getMessage());
        CommonResponse<Object> body = CommonResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .success(false)
                .message("Resource not found")
                .path(request.getRequestURI())
                .data(null)
                .build();
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Internal server error at {}: ", request.getRequestURI(), ex);
        CommonResponse<Object> body = CommonResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .success(false)
                .message("Internal server error")
                .path(request.getRequestURI())
                .data(null)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
