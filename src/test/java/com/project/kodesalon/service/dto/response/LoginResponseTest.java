package com.project.kodesalon.service.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class LoginResponseTest {

    @Test
    @DisplayName("회원 별명을 반환한다.")
    void getter() {
        String alias = "alias";
        LoginResponse loginResponse = new LoginResponse(alias);

        then(loginResponse.getAlias()).isEqualTo(alias);
    }
}
