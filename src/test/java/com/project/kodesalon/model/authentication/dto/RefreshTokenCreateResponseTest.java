package com.project.kodesalon.model.authentication.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class RefreshTokenCreateResponseTest {

    @Test
    @DisplayName("Refresh 토큰 값을 반환한다.")
    void getter() {
        RefreshTokenCreateResponse refreshTokenCreateResponse = new RefreshTokenCreateResponse("token");
        then(refreshTokenCreateResponse.getRefreshToken()).isEqualTo("token");
    }
}
