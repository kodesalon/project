package com.project.kodesalon.model.authentication.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRefreshRequest {

    @NotNull(message = INVALID_JWT_TOKEN)
    private String refreshToken;

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
