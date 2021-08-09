package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiBoardSelectResponse {
    private List<BoardSelectResponse> boards;
    private boolean isLast;

    public MultiBoardSelectResponse(List<BoardSelectResponse> boards, boolean isLast) {
        this.boards = boards;
        this.isLast = isLast;
    }
}
