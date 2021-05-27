package com.project.kodesalon.model.member.board.vo;

import com.project.kodesalon.model.board.domain.vo.Content;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class ContentTest {
    private String boardContent;
    private Content content;

    @BeforeEach
    void setUp() {
        boardContent = "test content";
        content = new Content(boardContent);
    }

    @Test
    @DisplayName("문자열을 입력받아 내용 객체를 생성한다.")
    public void create() {
        assertThat(content).isEqualTo(new Content(boardContent));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("공백 또는 아무것도 입력하지 않을 경우, 예외가 발생한다.")
    public void check_blank(String input) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Content(input))
                .withMessageContaining("내용에 공백 아닌 1자 이상의 문자를 입력");
    }

    @Test
    @DisplayName("내용의 문자열 값을 반환한다.")
    public void value() {
        String value = content.value();
        assertThat(value).isEqualTo(boardContent);
    }
}
