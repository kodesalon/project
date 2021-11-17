package com.project.kodesalon.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.kodesalon.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static com.project.kodesalon.domain.member.vo.Alias.ALIAS_REGEX;
import static com.project.kodesalon.domain.member.vo.Name.NAME_REGEX;
import static com.project.kodesalon.domain.member.vo.Password.PASSWORD_REGEX;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_ALIAS;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_EMAIL;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_NAME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_PASSWORD;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberCreateRequest {

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
    @Email(message = INVALID_MEMBER_EMAIL)
    private String email;

    private String phone;

    @NotNull(message = INVALID_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public MemberCreateRequest(final String alias, final String password, final String name, final String email, final String phone, final LocalDateTime createdDateTime) {
        this.alias = alias;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.createdDateTime = createdDateTime;
    }

    public Member toMember() {
        return new Member(alias, password, name, email, phone, createdDateTime);
    }
}
