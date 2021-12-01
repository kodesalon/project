package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse {

    private String alias;

    public LoginResponse(final String alias) {
        this.alias = alias;
    }
}
