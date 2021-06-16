package com.project.kodesalon.model.member.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ChangePasswordResponseTest {

    @Test
    @DisplayName("성공 메세지를 반환한다.")
    public void getter() {
        ChangePasswordResponse changePasswordResponse = new ChangePasswordResponse("비밀번호 변경 성공");
        then(changePasswordResponse.getMessage()).isEqualTo("비밀번호 변경 성공");
    }
}
