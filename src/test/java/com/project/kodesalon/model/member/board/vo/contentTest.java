package com.project.kodesalon.model.member.board.vo;

import com.project.kodesalon.model.board.domain.vo.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class contentTest {

    @Test
    @DisplayName("문자열을 입력받아 내용 객체를 생성한다.")
    public void create() {
        String boardContent = "test content";
        Content content = new Content(boardContent);
        assertThat(content).isEqualTo(new Content(boardContent));
    }
}
