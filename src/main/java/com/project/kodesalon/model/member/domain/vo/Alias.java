package com.project.kodesalon.model.member.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Alias {
    private static final String ALIAS_REGEX = "^[a-zA-Z]{1}[a-zA-Z0-9]{4,15}$";
    private static final Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_REGEX);
    private static final String INVALID_ALIAS_EXCEPTION_MESSAGE = "Alias 는 영문으로 시작해야 하며 4자리 이상 15자리 이하의 영문 혹은 숫자가 포함되어야 합니다.";

    @Column(name = "alias", nullable = false, unique = true, length = 15)
    private String alias;

    public Alias(final String alias) {
        if (!ALIAS_PATTERN.matcher(alias).matches()) {
            throw new IllegalArgumentException(INVALID_ALIAS_EXCEPTION_MESSAGE);
        }

        this.alias = alias;
    }

    public String getValue() {
        return alias;
    }
}
