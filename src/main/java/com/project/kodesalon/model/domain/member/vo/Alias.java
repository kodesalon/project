package com.project.kodesalon.model.domain.member.vo;

import java.util.IllegalFormatException;
import java.util.regex.Pattern;

public class Alias {
    private static final String ALIAS_PATTERN = "^[a-zA-Z]{1}[a-zA-Z0-9]{4,11}$";
    private static final String INVALID_ALIAS_EXCEPTION_MESSAGE = "잘못된 Alias 형식입니다.";

    private final String alias;

    public Alias (final String alias) {
        this.alias = validateAlias(alias);
    }

    public String value() {
        return alias;
    }

    private String validateAlias(String alias) throws IllegalFormatException {
        if (Pattern.matches(ALIAS_PATTERN, alias)) {
            return alias;
        }

        throw new RuntimeException(INVALID_ALIAS_EXCEPTION_MESSAGE);
    }
}
