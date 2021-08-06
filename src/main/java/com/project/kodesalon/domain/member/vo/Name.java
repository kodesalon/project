package com.project.kodesalon.domain.member.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

import static com.project.kodesalon.exception.ErrorCode.INVALID_MEMBER_NAME;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Name {

    public static final String NAME_REGEX = "^[가-힣]{2,17}";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    @Column(name = "name", nullable = false, length = 17)
    private String name;

    public Name(final String name) {
        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(INVALID_MEMBER_NAME);
        }

        this.name = name;
    }

    public String value() {
        return name;
    }
}
