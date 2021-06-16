package com.project.kodesalon.model.member.service.dto;

import com.project.kodesalon.model.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
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

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class CreateMemberRequest {

    @NotNull(message = "null이 아닌 4자리 이상의 아이디를 입력해주세요.")
    @Pattern(regexp = ALIAS_REGEX, message = INVALID_MEMBER_ALIAS)
    private String alias;

    @NotNull(message = "null이 아닌 8자리 이상의 비밀번호를 입력해주세요.")
    @Pattern(regexp = PASSWORD_REGEX, message = INVALID_MEMBER_PASSWORD)
    private String password;

    @NotNull(message = "null이 아닌 2자리 이상의 이름을 입력해주세요.")
    @Pattern(regexp = NAME_REGEX, message = INVALID_MEMBER_NAME)
    private String name;

    @NotEmpty(message = "null 또는 빈 공백이 아닌 이메일 주소를 입력해주세요.")
    @Email(message = INVALID_MEMBER_EMAIL)
    private String email;

    @NotNull(message = "null이 아닌 휴대폰 번호를 입력해주세요.")
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
