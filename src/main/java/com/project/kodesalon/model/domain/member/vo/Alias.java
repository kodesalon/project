package com.project.kodesalon.model.domain.member.vo;

import java.util.regex.Pattern;

public class Alias {
    private static final String ALIAS_REGEX= "^[a-zA-Z]{1}[a-zA-Z0-9]{4,11}$";
    private static final Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_REGEX);
    private static final String INVALID_ALIAS_EXCEPTION_MESSAGE = "잘못된 Alias 형식입니다.";

    private final String alias;

    public Alias (final String alias) {
        if (!ALIAS_PATTERN.matcher(alias).matches()) {
            throw new IllegalArgumentException(INVALID_ALIAS_EXCEPTION_MESSAGE);
        }
        this.alias = alias;
    }

    public String getValue() {
        return alias;
    }
}
