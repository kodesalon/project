package com.project.kodesalon.model.member.service.dto;

import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChangePasswordRequestDtoTest {

    @Test
    @DisplayName("회원 식별 번호, 변경하려는 비밀번호를 반환한다.")
    public void getter() {
        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto(1L, "ChangePassword1!");
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(changePasswordRequestDto.getMemberId()).isEqualTo(1L);
        softly.then(changePasswordRequestDto.getPassword()).isEqualTo("ChangePassword1!");
        softly.assertAll();
    }
}
