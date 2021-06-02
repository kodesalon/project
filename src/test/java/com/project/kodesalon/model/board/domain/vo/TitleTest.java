package com.project.kodesalon.model.board.domain.vo;

import com.project.kodesalon.model.board.exception.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TitleTest {
    private String titleValue;
    private Title title;

    @BeforeEach
    void setUp() {
        titleValue = "게시물 제목";
        title = new Title(titleValue);
    }

    @Test
    @DisplayName("문자열을 입력받아 제목 객체를 생성한다.")
    public void create() {
        assertThat(title).isEqualTo(new Title(titleValue));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null, 공백 또는 아무것도 입력하지 않을 경우, 예외가 발생한다.")
    public void check_null_or_blank(String input) {
        assertThatThrownBy(() -> new Title(input))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("제목에 공백 아닌 1자 이상의 문자를 입력");
    }

    @Test
    @DisplayName("제목의 길이가 30자를 초과할 경우, 예외가 발생한다.")
    public void check_length() {
        assertThatThrownBy(() -> new Title("this title length is 31        "))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessageContaining("제목 글자 수가 30을 초과");
    }

    @Test
    @DisplayName("제목의 문자열 값을 반환한다.")
    public void value() {
        String value = title.value();
        assertThat(value).isEqualTo(titleValue);
    }
}