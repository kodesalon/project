package com.project.kodesalon.model.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginResponseDtoTest {
    private static final Long MEMBER_ID = 1L;
    private static final String ALIAS = "alias";
    private static final String LOGIN_FAILED_MESSAGE = "로그인 실패";

    @Test
    @DisplayName("Loin이 성공하면 HttpStatus 200과 ID, ALIAS 객체를 생성합니다.")
    void login_success_create_success_login_response_dto() {
        LoginResponseDto loginResponseDto = new LoginResponseDto(MEMBER_ID, ALIAS);
        assertAll(
                () -> then(loginResponseDto.getMemberId()).isEqualTo(MEMBER_ID),
                () -> then(loginResponseDto.getAlias()).isEqualTo(ALIAS)
        );
    }

    @Test
    @DisplayName("Login이 실패하면 예외 메세지를 리턴합니다")
    void login_failed_create_failed_response_dto() {
        LoginResponseDto loginResponseDto = new LoginResponseDto(LOGIN_FAILED_MESSAGE);

        then(loginResponseDto.getMessage()).isEqualTo(LOGIN_FAILED_MESSAGE);
    }
}
