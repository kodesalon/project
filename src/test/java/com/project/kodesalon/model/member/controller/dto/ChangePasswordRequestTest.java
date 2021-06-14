package com.project.kodesalon.model.member.controller.dto;

import com.project.kodesalon.model.member.service.dto.ChangePasswordRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ChangePasswordRequestTest {
    private final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("ChangePassword1!");

    @Test
    @DisplayName("회원 식별 번호, 변경하려는 비밀번호를 반환한다.")
    public void getter() {
        then(changePasswordRequest.getPassword()).isEqualTo("ChangePassword1!");
    }

    @Test
    @DisplayName("ChangePasswordRequestDto를 반환한다.")
    public void toChangePasswordRequestDto() {
        ChangePasswordRequestDto changePasswordRequestDto = changePasswordRequest.toChangePasswordRequestDto();
        then(changePasswordRequestDto.getPassword()).isEqualTo("ChangePassword1!");
    }
}
