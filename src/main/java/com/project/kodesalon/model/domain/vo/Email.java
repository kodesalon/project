package com.project.kodesalon.model.domain.vo;

import java.util.regex.Pattern;

public class Email {
    private static final String EMAIL_EXCEPTION_MESSAGE = "잘못된 이메일 형식입니다";
    private static final String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private final String email;


    public Email (final String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException(EMAIL_EXCEPTION_MESSAGE);
        }

        this.email = email;
    }

    public String getValue() {
       return email;
    }
}
