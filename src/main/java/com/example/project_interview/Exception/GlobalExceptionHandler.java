package com.example.project_interview.Exception;

import com.example.project_interview.dto.CommonResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import org.springframework.security.core.AuthenticationException;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CommonResponse<Object>> handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Database error occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse("A database error occurred", status, request);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<CommonResponse<Object>> handleNoSuchElementException(NoSuchElementException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        log.warn("Resource not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), status, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        log.warn("Invalid input: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), status, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<CommonResponse<Object>> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        log.warn("Authentication failed: {}", ex.getMessage());
        return buildErrorResponse("Authentication failed: " + ex.getMessage(), status, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse<Object>> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        log.warn("Access denied: {}", ex.getMessage());
        return buildErrorResponse("Access denied: " + ex.getMessage(), status, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;
        log.warn("Data integrity violation: {}", ex.getMessage());
        return buildErrorResponse("Data integrity violation", status, request);
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<CommonResponse<Object>> handleMessagingException(MessagingException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Email sending failed: {}", ex.getMessage(), ex);
        return buildErrorResponse("Failed to send email", status, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        log.warn("Validation failed: {}", errors);
        return buildErrorResponse("Validation failed", status, errors, request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<CommonResponse<Object>> handleMaxSizeException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.PAYLOAD_TOO_LARGE;
        log.warn("File size exceeded: {}", ex.getMessage());
        return buildErrorResponse("File size exceeds the maximum allowed limit", status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Object>> handleAllUncaughtException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unhandled exception occurred: ", ex);
        return buildErrorResponse("An unexpected error occurred", status, request);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<CommonResponse<Object>> runtimeException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Unhandled exception occurred: ", ex);
        return buildErrorResponse(ex.getMessage(), status, request);
    }

    // Helper methods
    private ResponseEntity<CommonResponse<Object>> buildErrorResponse(String message, HttpStatus status, HttpServletRequest request) {
        return buildErrorResponse(message, status, null, request);
    }

    private ResponseEntity<CommonResponse<Object>> buildErrorResponse(String message, HttpStatus status, Object data, HttpServletRequest request) {
        CommonResponse<Object> body = CommonResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .success(false)
                .message(message)
                .path(request.getRequestURI())
                .data(data)
                .build();
        return ResponseEntity.status(status).body(body);
    }
    }


