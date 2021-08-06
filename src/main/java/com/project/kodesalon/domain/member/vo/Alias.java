package com.project.kodesalon.domain.member.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_ALIAS;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Alias {

    public static final String ALIAS_REGEX = "^[a-zA-Z]{1}[a-zA-Z0-9]{3,14}$";
    private static final Pattern ALIAS_PATTERN = Pattern.compile(ALIAS_REGEX);

    @Column(name = "alias", nullable = false, length = 15)
    private String alias;

    public Alias(final String alias) {
        if (alias == null || !ALIAS_PATTERN.matcher(alias).matches()) {
            throw new IllegalArgumentException(INVALID_MEMBER_ALIAS);
        }

        this.alias = alias;
    }

    public String value() {
        return alias;
    }
}
