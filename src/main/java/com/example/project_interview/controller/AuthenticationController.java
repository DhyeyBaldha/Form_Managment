package com.example.project_interview.controller;



import com.example.project_interview.auth.AuthenticationResponse;
import com.example.project_interview.entity.User;
import com.example.project_interview.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody User request){
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User request){
        System.out.println("Token from backend="+authenticationService.authenticate(request));
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

}

