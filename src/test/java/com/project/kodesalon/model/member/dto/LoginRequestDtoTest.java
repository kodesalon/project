package com.project.kodesalon.model.member.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

public class LoginRequestDtoTest {
    @Test
    @DisplayName("생성자가 초기화 되면 Alias와 Password를 초기화합니다.")
    void create_login_request_dto() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("alias", "Password1234!!");

        assertAll(
                () -> then(loginRequestDto.getAlias()).isEqualTo(new Alias("alias")),
                () -> then(loginRequestDto.getPassword()).isEqualTo(new Password("Password1234!!"))
        );
    }
}
