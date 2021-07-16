package com.project.kodesalon.model.board.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Title {
    public static final int TITLE_LENGTH_MAX_BOUND = 30;

    @Column(length = TITLE_LENGTH_MAX_BOUND, nullable = false)
    private String title;

    public Title(final String title) {
        validate(title);
        this.title = title;
    }

    private void validate(String title) {
        checkNullOrBlank(title);
        checkLength(title);
    }

    private void checkNullOrBlank(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(INVALID_BOARD_TITLE);
        }
    }

    private void checkLength(String title) {
        if (title.length() > TITLE_LENGTH_MAX_BOUND) {
            throw new IllegalArgumentException(INVALID_BOARD_TITLE);
        }
    }

    public String value() {
        return title;
    }
}
