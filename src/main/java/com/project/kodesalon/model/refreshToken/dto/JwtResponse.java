package com.project.kodesalon.model.refreshToken.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private Long memberId;
    private String alias;

    public JwtResponse(String accessToken, String refreshToken, Long memberId, String alias) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberId = memberId;
        this.alias = alias;
    }
}

