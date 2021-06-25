package com.project.kodesalon.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(SpringExtension.class)
class JwtIssueServiceTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JwtIssueService jwtIssueService = new JwtIssueService(objectMapper);
    private final LoginResponse loginResponse = new LoginResponse(1L, "alias");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtIssueService, "secretKey", "value");
    }

    @Test
    @DisplayName("access token을 발행한다.")
    void issueAccessToken() throws JsonProcessingException {
        String accessToken = jwtIssueService.issueAccessToken(loginResponse);

        then(accessToken).isNotNull();
    }

    @Test
    @DisplayName("refresh token을 발행한다.")
    void issueRefreshToken() throws JsonProcessingException {
        String refreshToken = jwtIssueService.issueRefreshToken(loginResponse);

        then(refreshToken).isNotNull();
    }
}
