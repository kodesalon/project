package com.project.kodesalon.common;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.BDDAssertions.then;

class JwtUtilsTest {

    private static final BDDSoftAssertions softly = new BDDSoftAssertions();
    private static final String JWT_SECERT_KEY = "bG9jYWxUZXN0S2V5";
    private static final Long MEMBER_ID = 1L;
    private static final int EXPIRATION_MS = 2000;

    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "secretKey", JWT_SECERT_KEY);
        ReflectionTestUtils.setField(jwtUtils, "accessExpirationMs", EXPIRATION_MS);
        ReflectionTestUtils.setField(jwtUtils, "refreshExpirationMs", EXPIRATION_MS);
    }

    @Test
    @DisplayName("Access token 토큰을 생성한다")
    void generateAccessToken() {
        String accessToken = jwtUtils.generateAccessToken(MEMBER_ID);
        then(accessToken).isNotEmpty();
    }

    @Test
    @DisplayName("Refresh token 토큰을 생성한다")
    void generateRefreshToken() {
        String refreshToken = jwtUtils.generateRefreshToken(MEMBER_ID);
        then(refreshToken).isNotEmpty();
    }

    @Test
    @DisplayName("토큰에 저장된 memberId를 반환한다.")
    void getMemberIdFrom() {
        String accessToken = jwtUtils.generateAccessToken(MEMBER_ID);
        String refreshToken = jwtUtils.generateRefreshToken(MEMBER_ID);

        softly.then(jwtUtils.getMemberIdFrom(accessToken)).isEqualTo(MEMBER_ID);
        softly.then(jwtUtils.getMemberIdFrom(refreshToken)).isEqualTo(MEMBER_ID);
        softly.assertAll();
    }

    @Test
    @DisplayName("토큰이 유효할 경우, 참을 반환한다.")
    void validateToken() throws InterruptedException {
        String accessToken = jwtUtils.generateAccessToken(MEMBER_ID);
        String refreshToken = jwtUtils.generateRefreshToken(MEMBER_ID);

        softly.then(jwtUtils.validateToken(accessToken)).isTrue();
        softly.then(jwtUtils.validateToken(refreshToken)).isTrue();
        Thread.sleep(EXPIRATION_MS);
        softly.then(jwtUtils.validateToken(accessToken)).isFalse();
        softly.then(jwtUtils.validateToken(refreshToken)).isFalse();
        softly.assertAll();
    }
}


