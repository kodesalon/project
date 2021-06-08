package com.project.kodesalon.model.member.controller.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginRequestTest {
    private LoginRequset loginRequset;

    @BeforeEach
    void setUp() {
        loginRequset = new LoginRequest("alias", "Password123!!");
    }

    @Test
    @DisplayName("생성자를 호출하면 필드가 초기화 됩니다")
    void create_init_field() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(loginRequest.getAlias()).isEqualTo("alias");
        softly.then(loginRequest.getPassword()).isEqualTo("Password123!!");

        softly.assertAll();
    }

    @Test
    @DisplayName("toLoginRequestDto를 호출하면 LoginRequestDto를 반환합니다")
    void to_login_request_dto_return_login_request_dto() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        LoginRequestDto loginRequestDto = loginRequset.toLoginRequestDto();

        softly.then(loginRequestDto.getAlias()).isEqualTo(new Alias("alias"));
        softly.then(loginRequestDto.getPassword()).isEqualTo(new Password("Password123!!"));

        softly.assertAll();
    }
}
