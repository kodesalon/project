package com.project.kodesalon.controller.authentication;

import com.project.kodesalon.service.authentication.AuthenticationTokenService;
import com.project.kodesalon.service.dto.request.LoginRequest;
import com.project.kodesalon.service.dto.request.TokenRefreshRequest;
import com.project.kodesalon.service.dto.response.LoginResponse;
import com.project.kodesalon.service.dto.response.TokenResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
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

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> reissueAccessAndRefreshToken(@RequestBody @Valid final TokenRefreshRequest tokenRefreshRequest) {
        TokenResponse tokenResponse = authenticationTokenService.reissueAccessAndRefreshToken(tokenRefreshRequest);
        return ResponseEntity.ok().body(tokenResponse);
    }
}
