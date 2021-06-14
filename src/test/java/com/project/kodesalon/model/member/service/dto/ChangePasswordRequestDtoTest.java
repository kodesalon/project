package com.project.kodesalon.model.member.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ChangePasswordRequestDtoTest {

    @Test
    @DisplayName("회원 식별 번호, 변경하려는 비밀번호를 반환한다.")
    public void getter() {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto("ChangePassword1!");

        then(changePasswordRequestDto.getPassword()).isEqualTo("ChangePassword1!");
    }
}
