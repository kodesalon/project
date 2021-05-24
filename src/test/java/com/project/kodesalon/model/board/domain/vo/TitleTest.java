package com.project.kodesalon.model.board.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TitleTest {

    @Test
    @DisplayName("문자열을 입력받아 제목 객체를 생성한다.")
    public void create() {
        String boardTitle = "test_board";
        Title title = new Title(boardTitle);
        assertThat(title).isEqualTo(new Title(boardTitle));
    }
}
