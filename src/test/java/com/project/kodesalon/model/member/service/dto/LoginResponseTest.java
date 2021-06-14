package com.project.kodesalon.model.member.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginResponseTest {
    @Test
    @DisplayName("Loin이 성공하면 ID, ALIAS 객체를 생성합니다.")
    void login_success_create_success_login_response_dto() {
        LoginResponse loginResponse = new LoginResponse(1L, "alias");
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(loginResponse.getMemberId()).isEqualTo(1L);
        softly.then(loginResponse.getAlias()).isEqualTo("alias");

        softly.assertAll();
    }
}
