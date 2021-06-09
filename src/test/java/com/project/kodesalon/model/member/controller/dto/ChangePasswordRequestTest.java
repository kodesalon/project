package com.project.kodesalon.model.member.controller.dto;

import com.project.kodesalon.model.member.service.dto.ChangePasswordRequestDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChangePasswordRequestTest {
    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(1L, "ChangePassword1!");

    @Test
    @DisplayName("회원 식별 번호, 변경하려는 비밀번호를 반환한다.")
    public void getter() {
        softly.then(changePasswordRequest.getMemberId()).isEqualTo(1L);
        softly.then(changePasswordRequest.getPassword()).isEqualTo("ChangePassword1!");
        softly.assertAll();
    }

    @Test
    @DisplayName("ChangePasswordRequestDto를 반환한다.")
    public void toChangePasswordRequestDto() {
        ChangePasswordRequestDto changePasswordRequestDto = changePasswordRequest.toChangePasswordRequestDto();
        softly.then(changePasswordRequestDto.getMemberId()).isEqualTo(1L);
        softly.then(changePasswordRequestDto.getPassword()).isEqualTo("ChangePassword1!");
        softly.assertAll();
    }
}
