package com.project.kodesalon.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.TokenDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
class JwtProviderTest {

    private final LoginResponse loginResponse = new LoginResponse(1L, "alias");
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @InjectMocks
    private JwtProvider jwtProvider;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtProvider, "secretKey", "value");
    }

    @Test
    @DisplayName("jwt 토큰을 반환합니다")
    void get_token() throws JsonProcessingException {
        TokenDto tokenDto = jwtProvider.createTokenResponse(loginResponse);

        softly.then(tokenDto.getAccessToken()).isNotNull();
        softly.then(tokenDto.getRefreshToken()).isNotNull();
        softly.assertAll();
    }
}
