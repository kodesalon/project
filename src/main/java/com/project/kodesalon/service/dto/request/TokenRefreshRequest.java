package com.project.kodesalon.service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;
import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TokenRefreshRequest {

    @NotNull(message = NOT_EXIST_MEMBER)
    private Long memberId;

    @NotNull(message = INVALID_JWT_TOKEN)
    private String refreshToken;

    public TokenRefreshRequest(final Long memberId, final String refreshToken) {
        this.memberId = memberId;
        this.refreshToken = refreshToken;
    }
}
