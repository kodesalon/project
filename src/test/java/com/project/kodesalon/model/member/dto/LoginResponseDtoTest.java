package com.project.kodesalon.model.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginResponseDtoTest {
    @Test
    @DisplayName("Loin이 성공하면 HttpStatus 200과 ID, ALIAS 객체를 생성합니다.")
    void login_success_create_success_login_response_dto() {
        LoginResponseDto loginResponseDto = new LoginResponseDto(1L, "alias");
        assertAll(
                () -> then(loginResponseDto.getMemberId()).isEqualTo(1L),
                () -> then(loginResponseDto.getAlias()).isEqualTo("alias")
        );
    }
}
