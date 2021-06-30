package com.project.kodesalon.model.authentication.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RefreshTokenCreateResponse {
    private String refreshToken;

    public RefreshTokenCreateResponse(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
