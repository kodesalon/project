package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_ID;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_ID;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDeleteRequest {

    @NotNull(message = INVALID_MEMBER_ID)
    private Long memberId;

    @NotNull(message = INVALID_BOARD_ID)
    private Long boardId;

    public BoardDeleteRequest(final Long memberId, final Long boardId) {
        this.memberId = memberId;
        this.boardId = boardId;
    }
}
