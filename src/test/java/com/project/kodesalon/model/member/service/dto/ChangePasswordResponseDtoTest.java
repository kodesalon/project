package com.project.kodesalon.model.member.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ChangePasswordResponseDtoTest {

    @Test
    @DisplayName("성공 메세지를 반환한다.")
    public void getter() {
        ChangePasswordResponseDto changePasswordResponseDto = new ChangePasswordResponseDto("비밀번호 변경 성공");
        then(changePasswordResponseDto.getMessage()).isEqualTo("비밀번호 변경 성공");
    }
}
