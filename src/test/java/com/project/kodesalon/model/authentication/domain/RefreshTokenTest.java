package com.project.kodesalon.model.authentication.domain;

import com.project.kodesalon.model.member.domain.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

public class RefreshTokenTest {
    private final Member member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444");
    private final LocalDateTime expiryDate = LocalDateTime.now();
    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = new RefreshToken(member, "token", expiryDate);

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
}
