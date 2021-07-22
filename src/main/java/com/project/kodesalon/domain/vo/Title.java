package com.project.kodesalon.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.project.kodesalon.common.code.ErrorCode.INVALID_BOARD_TITLE;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Title {
    public static final int TITLE_LENGTH_MAX_BOUND = 30;

    @Column(length = TITLE_LENGTH_MAX_BOUND, nullable = false)
    private String title;

    public Title(final String title) {
        validate(title);
        this.title = title;
    }

    private void validate(final String title) {
        checkNullOrBlank(title);
        checkLength(title);
    }

    private void checkNullOrBlank(final String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(INVALID_BOARD_TITLE);
        }
    }

    private void checkLength(final String title) {
        if (title.length() > TITLE_LENGTH_MAX_BOUND) {
            throw new IllegalArgumentException(INVALID_BOARD_TITLE);
        }
    }

    public String value() {
        return title;
    }
}
