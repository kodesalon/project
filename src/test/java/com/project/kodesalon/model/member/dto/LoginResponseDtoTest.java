package com.project.kodesalon.model.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginResponseDtoTest {
    private static final Long ID = 1L;
    private static final String ALIAS = "alias";
    private static final HttpStatus LOGIN_SUCCESS_STATUS = HttpStatus.OK;
    private static final HttpStatus LOGIN_FAILED_STATUS = HttpStatus.UNAUTHORIZED;

    @DisplayName("Loin이 성공하면 HttpStatus 200과 ID, ALIAS 객체를 생성합니다.")
    void login_success_create_success_login_response_dto() {
        LoginResponseDto loginResponseDto = new LoginResponseDto(LOGIN_SUCCESS_STATUS, ID, ALIAS);

        assertAll(
                then(loginResponseDto.getHttpStatus()).isEqualTo(LOGIN_SUCCESS_STATUS),
                then(loginResponseDto.getId()).isEqualTo(ID),
                then(loginResponseDto.getAlias()).isEqualTo(ALIAS)
        );
    }

    @DisplayName("Login이 실패하면 HttpStatus 401을 리턴합니다")
    void login_failed_create_failed_response_dto() {
        LoginResponseDto loginResponseDto = new LoginResponseDto(LOGIN_FAILED_STATUS);

        then(loginResponseDto.getHttpStatus()).isEqualTo(LOGIN_FAILED_STATUS);
    }
}
