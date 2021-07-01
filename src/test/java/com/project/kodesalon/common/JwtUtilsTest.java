package com.project.kodesalon.common;

import io.jsonwebtoken.JwtException;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static com.project.kodesalon.common.ErrorCode.EXPIRED_JWT_TOKEN;
import static org.assertj.core.api.BDDAssertions.then;

class JwtUtilsTest {

    private static final BDDSoftAssertions softly = new BDDSoftAssertions();
    private static final String JWT_SECERT_KEY = "bG9jYWxUZXN0S2V5";
    private static final Long MEMBER_ID = 1L;
    private static final int EXPIRATION_MS = 1000;

    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "secretKey", JWT_SECERT_KEY);
        ReflectionTestUtils.setField(jwtUtils, "accessExpirationMs", EXPIRATION_MS);
    }

    @Test
    @DisplayName("jwt token 토큰을 생성한다")
    void generateJwtToken() {
        String jwtToken = jwtUtils.generateJwtToken(MEMBER_ID);
        then(jwtToken).isNotEmpty();
    }

    @Test
    @DisplayName("토큰에 저장된 memberId를 반환한다.")
    void getMemberIdFrom() {
        String jwtToken = jwtUtils.generateJwtToken(MEMBER_ID);

        then(jwtUtils.getMemberIdFrom(jwtToken)).isEqualTo(MEMBER_ID);
    }

    @Test
    @DisplayName("토큰이 유효할 경우, 참을 반환한다.")
    void validateToken() throws InterruptedException {
        String jwtToken = jwtUtils.generateJwtToken(MEMBER_ID);

        softly.then(jwtUtils.validateToken(jwtToken)).isTrue();
        Thread.sleep(EXPIRATION_MS);
        softly.thenThrownBy(() -> jwtUtils.validateToken(jwtToken))
                .isInstanceOf(JwtException.class)
                .withFailMessage(EXPIRED_JWT_TOKEN);
        softly.assertAll();
    }
}


