package com.project.kodesalon.common;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

import static com.project.kodesalon.common.ErrorCode.EXPIRED_JWT_TOKEN;
import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class JwtUtilsTest {

    private static final String JWT_SECRET_KEY = "bG9jYWxUZXN0S2V5";
    private static final byte[] SECRET_KEY_BYTES = JWT_SECRET_KEY.getBytes();
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
    private static final Key SIGN_KEY = new SecretKeySpec(SECRET_KEY_BYTES, SIGNATURE_ALGORITHM.getJcaName());
    private static final Long MEMBER_ID = 1L;
    private static final int EXPIRATION_MS = 10000;
    private static final Date ISSUE_TIME = new Date();

    private final JwtUtils jwtUtils = new JwtUtils();

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "secretKey", JWT_SECRET_KEY);
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
    void validateToken() {
        String jwtToken = jwtUtils.generateJwtToken(MEMBER_ID);
        then(jwtUtils.validateToken(jwtToken)).isTrue();
    }

    @Test
    @DisplayName("jwt 토큰의 서명이 일치하지 않을 경우 예외를 발생시킨다.")
    void validateToken_throw_exception_with_invalid_signature() {
        String invalidJwtSecretKey = "as34gs45yHHHH256";
        byte[] invalidSecretKeyBytes = invalidJwtSecretKey.getBytes();
        Key invalidSignKey = new SecretKeySpec(invalidSecretKeyBytes, SIGNATURE_ALGORITHM.getJcaName());
        String invalidSignatureJwtToken = Jwts.builder()
                .setExpiration(new Date(ISSUE_TIME.getTime() + EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS384, invalidSignKey)
                .compact();

        thenThrownBy(() -> jwtUtils.validateToken(invalidSignatureJwtToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }

    @Test
    @DisplayName("jwt 토큰의 구조가 이상한 경우 예외를 발생시킨다.")
    void validateToken_throw_exception_with_malFormed_jwt_token() {
        String malFormedToken = "malFormedJwtToken";

        thenThrownBy(() -> jwtUtils.validateToken(malFormedToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }

    @Test
    @DisplayName("jwt 토큰이 만료될 경우 예외를 발생시킨다.")
    void validateToken_throw_exception_with_token_expired() {
        String expiredJwtToken = Jwts.builder()
                .setExpiration(new Date(ISSUE_TIME.getTime() - EXPIRATION_MS))
                .signWith(SIGNATURE_ALGORITHM, SIGN_KEY)
                .compact();

        thenThrownBy(() -> jwtUtils.validateToken(expiredJwtToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(EXPIRED_JWT_TOKEN);
    }

    @Test
    @DisplayName("서명되지 않은 jwt 토큰은 예외를 발생시킨다.")
    void validate_token_throw_exception_with_malFormed_token() {
        String unSupportedJwtToken = Jwts.builder()
                .setExpiration(new Date(ISSUE_TIME.getTime() + EXPIRATION_MS))
                .compact();

        thenThrownBy(() -> jwtUtils.validateToken(unSupportedJwtToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("jwt 토큰이 없거나 빈 문자열일 경우 예외를 발생시킨다.")
    void validateToken_throw_exception_with_null_and_empty_token(String invalidJwtToken) {
        thenThrownBy(() -> jwtUtils.validateToken(invalidJwtToken))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }
}


