package com.project.kodesalon.domain.authentication;

import com.project.kodesalon.domain.member.Member;
import io.jsonwebtoken.JwtException;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;
import static com.project.kodesalon.utils.TestEntityUtils.getTestMember;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class RefreshTokenTest {
    private static final Member TEST_MEMBER = getTestMember();

    private final LocalDateTime expiryDate = LocalDateTime.now();
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = new RefreshToken(TEST_MEMBER, "token", expiryDate);
    }

    @Test
    @DisplayName("Refresh Token의 요소 값을 반환한다. ")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(refreshToken.getToken()).isEqualTo("token");
        softly.then(refreshToken.getExpiryDate()).isEqualTo(expiryDate);
        softly.assertAll();
    }

    @Test
    @DisplayName("인자로 받은 새로운 토큰 값으로 변경한다.")
    void replace() {
        String newToken = "new token";
        refreshToken.replace(newToken);
        then(refreshToken.getToken()).isEqualTo(newToken);
    }

    @Test
    @DisplayName("인자로 받은 시간과 토큰 만료시간을 비교하여 토큰이 만료되었을 경우, 예외를 발생시킨다.")
    void validate_throw_exception_with_expired_time() {
        LocalDateTime now = LocalDateTime.now().plus(expiryDate.getNano(), ChronoUnit.NANOS);
        thenThrownBy(() -> refreshToken.validateExpiryDate(now))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }
}
