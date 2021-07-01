package com.project.kodesalon.model.authentication.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class TokenRefreshRequestTest {

    @Test
    @DisplayName("Refresh 토큰 값을 반환한다.")
    void getter() {
        TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("token");
        then(tokenRefreshRequest.getRefreshToken()).isEqualTo("token");
    }
}
