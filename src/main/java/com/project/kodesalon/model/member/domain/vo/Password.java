package com.project.kodesalon.model.member.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final String PASSWORD_EXCEPTION_MESSAGE = "잘못된 Password 형식입니다.";

    @Column(name = "password", nullable = false, length = 30)
    private String password;

    public Password(final String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(PASSWORD_EXCEPTION_MESSAGE);
        }
        this.password = password;
    }

    public String getValue() {
        return password;
    }
}