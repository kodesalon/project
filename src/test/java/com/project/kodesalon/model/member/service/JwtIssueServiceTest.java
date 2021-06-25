package com.project.kodesalon.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class JwtIssueServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtIssueService jwtIssueService = new JwtIssueService(objectMapper);
    private final LoginResponse loginResponse = new LoginResponse(1L, "alias");

    @Test
    @DisplayName("access token을 발행합니다")
    void issueAccessToken() throws JsonProcessingException {
        String accessToken = jwtIssueService.issueAccessToken(loginResponse);

        then(accessToken).isNotNull();
    }

    @Test
    @DisplayName("")
    void issueRefreshToken() throws JsonProcessingException {
        String refreshToken = jwtIssueService.issueRefreshToken(loginResponse);

        then(refreshToken).isNotNull();
    }
}
