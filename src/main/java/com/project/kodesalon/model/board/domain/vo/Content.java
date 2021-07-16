package com.project.kodesalon.model.board.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Content {
    public static final int CONTENT_LENGTH_BOUND_MAX = 500;

    @Lob
    @Column(nullable = false, length = CONTENT_LENGTH_BOUND_MAX)
    private String content;

    public Content(String content) {
        validate(content);
        this.content = content;
    }

    private void validate(String content) {
        checkNullOrBlank(content);
        checkLength(content);
    }

    private void checkNullOrBlank(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(INVALID_BOARD_CONTENT);
        }
    }

    private void checkLength(String content) {
        if (content.length() > CONTENT_LENGTH_BOUND_MAX) {
            throw new IllegalArgumentException(INVALID_BOARD_CONTENT);
        }
    }

    public String value() {
        return content;
    }
}
