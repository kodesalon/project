package com.project.kodesalon.model.board.domain.vo;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Title {
    private static final String CHECK_TITLE_IS_BLANK = "제목에 공백 아닌 1자 이상의 문자를 입력하였는지 확인해주세요.";
    private static final int TITLE_LENGTH_MAX_BOUND = 30;

    @Column(length = TITLE_LENGTH_MAX_BOUND, nullable = false)
    private String title;

    public Title(final String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException(CHECK_TITLE_IS_BLANK);
        }

        this.title = title;
    }
}
