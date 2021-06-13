package com.project.kodesalon.model.member.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Name {
    private static final String NAME_REGEX = "^[가-힣]{2,17}";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    @Column(name = "name", nullable = false, length = 17)
    private String name;

    public Name(final String name) {
        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("이름은 2자리 이상 17자리 이하의 한글이어야 합니다.");
        }

        this.name = name;
    }

    public String value() {
        return name;
    }
}
