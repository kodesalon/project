package com.project.kodesalon.model.member.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginRequestDtoTest {
    private static final String ALIAS = "alias";
    private static final String PASSWORD = "Password1234!!";

    @Test
    @DisplayName("생성자가 초기화 되면 Alias와 Password를 초기화합니다.")
    void create_login_request_dto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(ALIAS, PASSWORD);

        assertAll(
                () -> then(loginRequestDto.getAlias()).isEqualTo(ALIAS),
                () -> then(loginRequestDto.getPassword()).isEqualTo(PASSWORD)
        );
    }
}
