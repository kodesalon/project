package com.project.kodesalon.model.refreshToken.domain;

import com.project.kodesalon.model.member.domain.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class RefreshTokenTest {

    @Test
    @DisplayName("Refresh Token의 요소 값을 반환한다. ")
    void getter() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        Member member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444");
        LocalDateTime expiryDate = LocalDateTime.now();

        RefreshToken refreshToken = new RefreshToken(member, "token", expiryDate);

        softly.then(refreshToken.getToken()).isEqualTo("token");
        softly.then(refreshToken.getExpiryDate()).isEqualTo(expiryDate);
        softly.assertAll();
    }
}
