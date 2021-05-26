package com.project.kodesalon.model.board.domain.vo;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Title {
    private static final String CHECK_TITLE_IS_BLANK = "제목에 공백 아닌 1자 이상의 문자를 입력하였는지 확인해주세요.";
    private static final int TITLE_LENGTH_MAX_BOUND = 30;

    @Column(length = TITLE_LENGTH_MAX_BOUND)
    private String title;

    public Title(final String title) {
        validate(title);
        this.title = title;
    }

    private void validate(String title) {
        checkNullOrBlank(title);
    }

    private void checkNullOrBlank(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(CHECK_TITLE_IS_BLANK);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title1 = (Title) o;
        return Objects.equals(title, title1.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
