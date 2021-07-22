package com.project.kodesalon.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

import static com.project.kodesalon.common.code.ErrorCode.INVALID_MEMBER_PASSWORD;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Password {
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    @Column(name = "password", nullable = false, length = 16)
    private String password;

    public Password(final String password) {
        if (password == null || !PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException(INVALID_MEMBER_PASSWORD);
        }
        this.password = password;
    }

    public String value() {
        return password;
    }

    public boolean isSame(String password) {
        return this.password.equals(password);
    }
}
