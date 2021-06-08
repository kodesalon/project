package com.project.kodesalon.model.member.controller.dto;

import com.project.kodesalon.model.member.dto.LoginRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginRequest {
    private String alias;
    private String password;

    public LoginRequest(String alias, String password) {
        this.alias = alias;
        this.password = password;
    }

    public LoginRequestDto toLoginRequestDto() {
        return new LoginRequestDto(alias, password);
    }
}
