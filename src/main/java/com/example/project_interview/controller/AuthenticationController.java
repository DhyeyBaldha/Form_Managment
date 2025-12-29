package com.example.project_interview.controller;



import com.example.project_interview.auth.AuthenticationResponse;
import com.example.project_interview.dto.CommonResponse;
import com.example.project_interview.dto.LoginDto;
import com.example.project_interview.entity.User;
import com.example.project_interview.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse<AuthenticationResponse>> register(@RequestBody User request, HttpServletRequest httpRequest){
        AuthenticationResponse data = authenticationService.register(request);
        return ResponseEntity.ok(
                CommonResponse.<AuthenticationResponse>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("User registered")
                        .path(httpRequest.getRequestURI())
                        .data(data)
                        .build()
        );
    }
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthenticationResponse>> login(@RequestBody LoginDto request, HttpServletRequest httpRequest){
        AuthenticationResponse data = authenticationService.authenticate(request);
        return ResponseEntity.ok(
                CommonResponse.<AuthenticationResponse>builder()
                        .timestamp(Instant.now())
                        .status(200)
                        .success(true)
                        .message("Login successful")
                        .path(httpRequest.getRequestURI())
                        .data(data)
                        .build()
        );
    }

}

