package com.project.kodesalon.model.authentication.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenRefreshRequest {

    @NotNull(message = INVALID_JWT_TOKEN)
    private String refreshToken;

    public TokenRefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
