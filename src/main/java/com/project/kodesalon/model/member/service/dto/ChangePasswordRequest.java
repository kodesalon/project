package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.model.member.domain.vo.Password.PASSWORD_REGEX;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @NotNull(message = INVALID_MEMBER_PASSWORD)
    @Pattern(regexp = PASSWORD_REGEX, message = INVALID_MEMBER_PASSWORD)
    private String password;

    public ChangePasswordRequest(final String password) {
        this.password = password;
    }
}
