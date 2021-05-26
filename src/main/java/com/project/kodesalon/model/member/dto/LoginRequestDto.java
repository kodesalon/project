package com.project.kodesalon.model.member.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestDto {
    @Getter
    private String alias;

    @Getter
    private String password;

    public LoginRequestDto(String alias, String password) {
        this.alias = alias;
        this.password = password;
    }
}
