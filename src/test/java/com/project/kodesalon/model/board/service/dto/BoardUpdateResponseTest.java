package com.project.kodesalon.model.board.service.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

public class BoardUpdateResponseTest {
    private final BoardUpdateResponse boardUpdateResponse = new BoardUpdateResponse("게시물 정보가 변경되었습니다");

    @Test
    @DisplayName("성공 메세지를 반환합니다.")
    void getter() {
        then(boardUpdateResponse.getMessage()).isEqualTo("게시물 정보가 변경되었습니다");
    }
}
