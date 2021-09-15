package com.project.kodesalon.domain.board.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_TITLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TitleTest {

    private String titleValue;
    private Title title;

    @BeforeEach
    void setUp() {
        titleValue = "게시물 제목";
        title = new Title(titleValue);
    }

    @Test
    @DisplayName("문자열을 입력받아 제목 객체를 생성한다.")
    void create() {
        assertThat(title).isEqualTo(new Title(titleValue));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("null, 공백 또는 아무것도 입력하지 않을 경우, 예외가 발생한다.")
    void check_null_or_blank(String input) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Title(input))
                .withMessageContaining(INVALID_BOARD_TITLE);
    }

    @Test
    @DisplayName("제목의 길이가 30자를 초과할 경우, 예외가 발생한다.")
    void check_length() {
        assertThatIllegalArgumentException().isThrownBy(() -> new Title("this title length is 31        "))
                .withMessageContaining(INVALID_BOARD_TITLE);
    }

    @Test
    @DisplayName("제목의 문자열 값을 반환한다.")
    void value() {
        String value = title.value();
        assertThat(value).isEqualTo(titleValue);
    }
}
