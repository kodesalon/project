package com.project.kodesalon.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.TokenDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
class JwtServiceTest {

    private final LoginResponse loginResponse = new LoginResponse(1L, "alias");
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private JwtIssueService jwtIssueService;

    @Test
    @DisplayName("jwt 토큰을 반환합니다")
    void get_token() throws JsonProcessingException {
        given(jwtIssueService.issueAccessToken(any(LoginResponse.class))).willReturn("access token");
        given(jwtIssueService.issueRefreshToken(any(LoginResponse.class))).willReturn("refresh token");
        TokenDto tokenDto = jwtService.createTokenResponse(loginResponse);

        softly.then(tokenDto.getAccessToken()).isNotNull();
        softly.then(tokenDto.getRefreshToken()).isNotNull();
        softly.assertAll();
    }
}
