package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiBoardSelectResponse {
    private List<BoardSelectResponse> boards;
    private boolean isLast;

    public MultiBoardSelectResponse(final List<BoardSelectResponse> boards, final int size) {
        this.isLast = hasLast(boards, size);
        this.boards = removeLastIfOverloaded(boards);
    }

    private boolean hasLast(final List<BoardSelectResponse> boards, final int size) {
        return boards.size() <= size;
    }

    private List<BoardSelectResponse> removeLastIfOverloaded(final List<BoardSelectResponse> boards) {
        LinkedList<BoardSelectResponse> boardSelectResponses = new LinkedList<>(boards);

        if (!isLast) {
            boardSelectResponses.removeLast();
        }

        return Collections.unmodifiableList(boardSelectResponses);
    }
}
