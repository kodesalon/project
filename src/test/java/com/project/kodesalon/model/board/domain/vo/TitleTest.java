package com.project.kodesalon.model.board.domain.vo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class TitleTest {
    private String boardTitle;
    private Title title;

    @BeforeEach
    void setUp() {
        boardTitle = "test_board";
        title = new Title(boardTitle);
    }

    @Test
    @DisplayName("문자열을 입력받아 제목 객체를 생성한다.")
    public void create() {
        assertThat(title).isEqualTo(new Title(boardTitle));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("공백 또는 아무것도 입력하지 않을 경우, 예외가 발생한다.")
    public void check_blank(String blank) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Title(blank))
                .withMessageContaining("제목에 공백 아닌 1자 이상의 문자를 입력");
    }

    @Test
    @DisplayName("제목의 문자열 값을 반환한다.")
    public void getValue() {
        String value = title.getValue();
        assertThat(value).isEqualTo(boardTitle);
    }
}
