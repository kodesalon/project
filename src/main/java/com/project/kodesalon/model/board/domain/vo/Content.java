package com.project.kodesalon.model.board.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Content {
    public static final String CHECK_CONTENT_IS_BLANK = "내용에 공백 아닌 1자 이상의 문자를 입력하였는지 확인해주세요.";
    public static final String CHECK_CONTENT_LENGTH = "내용이 500자를 초과하는지 확인해주세요.";
    public static final int CONTENT_LENGTH_BOUND_MAX = 500;

    @Lob
    @Column(nullable = false, length = CONTENT_LENGTH_BOUND_MAX)
    private String content;

    public Content(final String content) {
        validate(content);
        this.content = content;
    }

    private void validate(final String content) {
        checkNullOrBlank(content);
        checkLength(content);
    }

    private void checkNullOrBlank(final String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException(CHECK_CONTENT_IS_BLANK);
        }
    }

    private void checkLength(final String content) {
        if (content.length() > CONTENT_LENGTH_BOUND_MAX) {
            throw new IllegalArgumentException(CHECK_CONTENT_LENGTH);
        }
    }

    public String value() {
        return content;
    }
}
