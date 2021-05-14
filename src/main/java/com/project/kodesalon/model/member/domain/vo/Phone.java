package com.project.kodesalon.model.member.domain.vo;

import java.util.regex.Pattern;

public class Phone {
    private static final String PHONE_ERROR_MESSAGE = "잘못된 Phone 형식입니다.";
    private static final String PHONE_REGEX = "^01(?:0|1|[6-9])[-](\\d{3}|\\d{4})[-](\\d{4})";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    private final String phone;

    public Phone (final String phone) {
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException(PHONE_ERROR_MESSAGE);
        }

        this.phone = phone;
    }

    public String getValue() {
        return phone;
    }
}
