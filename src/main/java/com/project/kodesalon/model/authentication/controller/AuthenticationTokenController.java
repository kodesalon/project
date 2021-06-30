package com.project.kodesalon.model.authentication.controller;

import com.project.kodesalon.model.authentication.service.AuthenticationTokenService;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationTokenController {

    private final AuthenticationTokenService authenticationTokenService;

    public AuthenticationTokenController(AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid final LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationTokenService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }
}
