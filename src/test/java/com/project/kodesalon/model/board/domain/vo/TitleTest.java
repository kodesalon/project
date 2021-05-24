package com.project.kodesalon.model.board.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class TitleTest {

    @Test
    @DisplayName("문자열을 입력받아 제목 객체를 생성한다.")
    public void create() {
        String boardTitle = "test_board";
        Title title = new Title(boardTitle);
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
    @DisplayName("제목의 길이가 30자를 초과할 경우, 예외가 발생한다.")
    public void check_length() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Title("this title length is 31        "))
                .withMessageContaining("제목 글자 수가 30을 초과");
    }
}
