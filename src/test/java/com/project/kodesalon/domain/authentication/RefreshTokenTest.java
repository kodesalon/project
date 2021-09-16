package com.project.kodesalon.domain.authentication;

import io.jsonwebtoken.JwtException;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

class RefreshTokenTest {

    @Test
    @DisplayName("새로 발행된 토큰 정보를 반환한다.")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        LocalDateTime expiryDate = LocalDateTime.now();
        RefreshToken refreshToken = new RefreshToken(1L, "token", expiryDate);

        softly.then(refreshToken.getToken()).isEqualTo("token");
        softly.then(refreshToken.getExpiryDate()).isEqualTo(expiryDate);
        softly.assertAll();
    }

    @Test
    @DisplayName("인자로 받은 새로운 토큰 값으로 변경한다.")
    void replace() {
        String newToken = "new token";
        LocalDateTime expiryDate = LocalDateTime.now();
        RefreshToken refreshToken = new RefreshToken(1L, "token", expiryDate);

        refreshToken.replace(newToken);

        then(refreshToken.getToken()).isEqualTo(newToken);
    }

    @Test
    @DisplayName("인자로 받은 시간과 토큰 만료시간을 비교하여 토큰이 만료되었을 경우, 예외를 발생시킨다.")
    void validate_throw_exception_with_expired_time() {
        LocalDateTime expiryDate = LocalDateTime.now();
        RefreshToken refreshToken = new RefreshToken(1L, "token", expiryDate);
        LocalDateTime now = LocalDateTime.now().plus(expiryDate.getNano(), ChronoUnit.NANOS);

        thenThrownBy(() -> refreshToken.validateExpiryDate(now))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }
}
