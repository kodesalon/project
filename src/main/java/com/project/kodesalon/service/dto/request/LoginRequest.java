package com.project.kodesalon.service.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.project.kodesalon.domain.member.vo.Alias.ALIAS_REGEX;
import static com.project.kodesalon.domain.member.vo.Password.PASSWORD_REGEX;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotNull(message = INVALID_MEMBER_ALIAS)
    @Pattern(regexp = ALIAS_REGEX, message = INVALID_MEMBER_ALIAS)
    private String alias;

    @NotNull(message = INVALID_MEMBER_PASSWORD)
    @Pattern(regexp = PASSWORD_REGEX, message = INVALID_MEMBER_PASSWORD)
    private String password;

    public LoginRequest(final String alias, final String password) {
        this.alias = alias;
        this.password = password;
    }
}
