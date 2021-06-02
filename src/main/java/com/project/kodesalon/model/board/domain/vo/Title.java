package com.project.kodesalon.model.board.domain.vo;

import com.project.kodesalon.model.board.exception.InvalidArgumentException;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static java.lang.String.format;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Title {
    private static final String CHECK_TITLE_IS_BLANK = "제목에 공백 아닌 1자 이상의 문자를 입력하였는지 확인해주세요.";
    private static final String CHECK_TITLE_LENGTH = "제목 글자 수가 %d을 초과하였는지 확인해주세요.";
    private static final int TITLE_LENGTH_MAX_BOUND = 30;

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
            throw new InvalidArgumentException(CHECK_TITLE_IS_BLANK);
        }
    }

    private void checkLength(String title) {
        if (title.length() > TITLE_LENGTH_MAX_BOUND) {
            throw new InvalidArgumentException(format(CHECK_TITLE_LENGTH, TITLE_LENGTH_MAX_BOUND));
        }
    }

    public String value() {
        return title;
    }
}
