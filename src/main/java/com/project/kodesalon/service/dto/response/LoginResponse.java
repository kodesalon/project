package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String alias;

    public LoginResponse(String accessToken, String refreshToken, Long memberId, String alias) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.alias = alias;
    }
}
