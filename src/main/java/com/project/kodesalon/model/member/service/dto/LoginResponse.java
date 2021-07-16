package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
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
