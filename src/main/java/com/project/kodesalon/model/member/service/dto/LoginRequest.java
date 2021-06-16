package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.model.member.domain.vo.Alias.ALIAS_REGEX;
import static com.project.kodesalon.model.member.domain.vo.Password.PASSWORD_REGEX;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotNull(message = "null이 아닌 4자리 이상의 아이디를 입력해주세요.")
    @Pattern(regexp = ALIAS_REGEX, message = INVALID_MEMBER_ALIAS)
    private String alias;

    @NotNull(message = "null이 아닌 8자리 이상의 비밀번호를 입력해주세요.")
    @Pattern(regexp = PASSWORD_REGEX, message = INVALID_MEMBER_PASSWORD)
    private String password;

    public LoginRequest(final String alias, final String password) {
        this.alias = alias;
        this.password = password;
    }
}
