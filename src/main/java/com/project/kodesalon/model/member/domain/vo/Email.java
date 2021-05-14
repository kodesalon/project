package com.project.kodesalon.model.member.domain.vo;

import javax.persistence.*;
import java.util.regex.Pattern;

@Embeddable
@AttributeOverride(name = "email", column = @Column(name = "email"))
@Access(AccessType.FIELD)
public class Email {
    private static final String EMAIL_EXCEPTION_MESSAGE = "잘못된 이메일 형식입니다";
    private static final String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String email;


    public Email (final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(EMAIL_EXCEPTION_MESSAGE);
        }

        this.email = email;
    }

    public String getValue() {
       return email;
    }
}
