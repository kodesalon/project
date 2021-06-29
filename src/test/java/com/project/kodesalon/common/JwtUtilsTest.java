package com.project.kodesalon.common;

import com.project.kodesalon.model.member.service.dto.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.BDDAssertions.then;

class JwtUtilsTest {

    private static final String JWT_SECERT_KEY = "bG9jYWxUZXN0S2V5";
    private static final int JWT_EXPIRATION_MS = 1000;

    private final LoginResponse loginResponse = new LoginResponse(1L, "alias");
    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "secretKey", JWT_SECERT_KEY);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", JWT_EXPIRATION_MS);
    }

    @Test
    @DisplayName("jwt(access token) 토큰을 생성한다")
    void generateJwtToken() {
        String jwtToken = jwtUtils.generateJwtToken(loginResponse);
        then(jwtToken).isNotEmpty();
    }

    @Test
    @DisplayName("토큰에 저장된 memberId를 반환한다.")
    void getMemberIdFrom() {
        String jwtToken = jwtUtils.generateJwtToken(loginResponse);
        Long memberId = jwtUtils.getMemberIdFrom(jwtToken);

        then(memberId).isEqualTo(loginResponse.getMemberId());
    }

    @Test
    @DisplayName("토큰이 유효할 경우, 참을 반환한다.")
    void validateToken() throws InterruptedException {
        String jwtToken = jwtUtils.generateJwtToken(loginResponse);

        then(jwtUtils.validateToken(jwtToken)).isTrue();
        Thread.sleep(JWT_EXPIRATION_MS);
        then(jwtUtils.validateToken(jwtToken)).isFalse();
    }
}


