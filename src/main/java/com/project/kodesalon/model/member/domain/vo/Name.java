package com.project.kodesalon.model.member.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Name {
    private static final String NAME_EXCEPTION_MESSAGE= "잘못된 Name 형식입니다.";
    private static final String NAME_REGEX = "^[가-힣]{2,17}";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    public Name (final String name) {
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(NAME_EXCEPTION_MESSAGE);
        }

        this.name = name;
    }

    public String getValue() {
        return name;
    }
}
