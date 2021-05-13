package com.project.kodesalon.model.domain.vo;

public class Email {
    private final String email;
    private static final String EMAIL_EXCEPTION_MESSAGE = "잘못된 이메일 형식입니다";

    public Email (final String email) {
        this.email = email;
    }

    public String getValue() {
       return email;
    }
}
