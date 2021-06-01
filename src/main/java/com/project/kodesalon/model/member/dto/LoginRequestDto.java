package com.project.kodesalon.model.member.dto;

import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Password;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequestDto {
    @Getter
    private Alias alias;

    @Getter
    private Password password;

    public LoginRequestDto(String alias, String password) {
        this.alias = new Alias(alias);
        this.password = new Password(password);
    }
}
