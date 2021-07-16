package com.project.kodesalon.model.authentication.controller;

import com.project.kodesalon.model.authentication.service.AuthenticationTokenService;
import com.project.kodesalon.model.authentication.service.dto.JwtResponse;
import com.project.kodesalon.model.authentication.service.dto.LoginRequest;
import com.project.kodesalon.model.authentication.service.dto.LoginResponse;
import com.project.kodesalon.model.authentication.service.dto.TokenRefreshRequest;
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

    public AuthenticationTokenController(final AuthenticationTokenService authenticationTokenService) {
        this.authenticationTokenService = authenticationTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid final LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationTokenService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<JwtResponse> reissueAccessAndRefreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest) {
        JwtResponse jwtResponse = authenticationTokenService.reissueAccessAndRefreshToken(tokenRefreshRequest);
        return ResponseEntity.ok().body(jwtResponse);
    }
}
