package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiBoardSelectResponse {
    private List<BoardSelectResponse> boards;
    private boolean last;
    private int size;

    public MultiBoardSelectResponse(final List<BoardSelectResponse> boards, final int size) {
        this.boards = boards;
        this.size = size;
        this.last = isLastBoard();
    }

    private boolean isLastBoard() {
        return boards.size() <= size;
    }

    public List<BoardSelectResponse> getBoards() {
        if (last) {
            return boards;
        }

        return boards.subList(0, size);
    }

    public boolean isLast() {
        return last;
    }
}
