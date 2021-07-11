package com.project.kodesalon.model.authentication.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginResponseTest {
    @Test
    @DisplayName("Loin이 성공하면 ID, ALIAS 객체를 생성합니다.")
    void login_success_create_success_login_response_dto() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        LoginResponse loginResponse = new LoginResponse("access token", "refresh token", 1L, "alias");

        softly.then(loginResponse.getAccessToken()).isEqualTo("access token");
        softly.then(loginResponse.getRefreshToken()).isEqualTo("refresh token");
        softly.then(loginResponse.getMemberId()).isEqualTo(1L);
        softly.then(loginResponse.getAlias()).isEqualTo("alias");
        softly.assertAll();
    }
}
