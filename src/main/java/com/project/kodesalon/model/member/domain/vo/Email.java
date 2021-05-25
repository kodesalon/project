package com.project.kodesalon.model.member.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Email {
    private static final String EMAIL_EXCEPTION_MESSAGE = "Email은 이메일주소@회사.com 형식 이어야 합니다.";
    private static final String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @Column(name = "email", nullable = false)
    private String email;

    public Email(final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(EMAIL_EXCEPTION_MESSAGE);
        }

        this.email = email;
    }

    public String getValue() {
        return email;
    }
}
