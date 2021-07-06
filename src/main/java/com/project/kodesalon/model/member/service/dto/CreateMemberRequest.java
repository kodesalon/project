package com.project.kodesalon.model.member.service.dto;

import com.project.kodesalon.model.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_EMAIL;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_NAME;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PASSWORD;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_PHONE;
import static com.project.kodesalon.model.member.domain.vo.Alias.ALIAS_REGEX;
import static com.project.kodesalon.model.member.domain.vo.Name.NAME_REGEX;
import static com.project.kodesalon.model.member.domain.vo.Password.PASSWORD_REGEX;
import static com.project.kodesalon.model.member.domain.vo.Phone.PHONE_REGEX;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateMemberRequest {

    @NotNull(message = INVALID_MEMBER_ALIAS)
    @Pattern(regexp = ALIAS_REGEX, message = INVALID_MEMBER_ALIAS)
    private String alias;

    @NotNull(message = INVALID_MEMBER_PASSWORD)
    @Pattern(regexp = PASSWORD_REGEX, message = INVALID_MEMBER_PASSWORD)
    private String password;

    @NotNull(message = INVALID_MEMBER_NAME)
    @Pattern(regexp = NAME_REGEX, message = INVALID_MEMBER_NAME)
    private String name;

    @NotEmpty(message = INVALID_MEMBER_EMAIL)
    @javax.validation.constraints.Email(message = INVALID_MEMBER_EMAIL)
    private String email;

    @NotNull(message = INVALID_MEMBER_PHONE)
    @Pattern(regexp = PHONE_REGEX, message = INVALID_MEMBER_PHONE)
    private String phone;

    public CreateMemberRequest(final String alias, final String password, final String name, final String email, final String phone) {
        this.alias = alias;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Member toMember() {
        return new Member(alias, password, name, email, phone);
    }
}
