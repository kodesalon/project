package com.project.kodesalon.model.authentication.domain;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.BDDAssertions.then;

public class RefreshTokenTest {
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
    @DisplayName("인자로 받은 시간과 토큰 만료시간을 비교하여 토큰이 만료되었을 경우, 참을 반환한다.")
    void isAfter() {
        LocalDateTime now = LocalDateTime.now().minus(expiryDate.getNano(), ChronoUnit.NANOS);
        then(refreshToken.isAfter(now)).isTrue();
    }
}
