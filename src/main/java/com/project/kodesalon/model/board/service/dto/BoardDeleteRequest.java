package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDeleteRequest {
    private Long memberId;
    private Long boardId;

    public BoardDeleteRequest(Long memberId, Long boardId) {
        this.memberId = memberId;
        this.boardId = boardId;
    }
}
