package com.project.kodesalon.model.member.board.vo;

import com.project.kodesalon.model.board.domain.vo.Content;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class contentTest {

    @Test
    @DisplayName("문자열을 입력받아 내용 객체를 생성한다.")
    public void create() {
        String boardContent = "test content";
        Content content = new Content(boardContent);
        assertThat(content).isEqualTo(new Content(boardContent));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("공백 또는 아무것도 입력하지 않을 경우, 예외가 발생한다.")
    public void check_blank(String blank) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Content(blank))
                .withMessageContaining("내용에 공백 아닌 1자 이상의 문자를 입력");
    }
}
