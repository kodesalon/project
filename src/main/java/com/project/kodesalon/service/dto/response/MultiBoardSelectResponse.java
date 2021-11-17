package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiBoardSelectResponse<E> {
    private List<E> boards;
    private boolean isLast;

    public MultiBoardSelectResponse(final List<E> boards, final long size) {
        this.isLast = hasLast(boards, size);
        this.boards = removeLastIfOverloaded(boards);
    }

    private boolean hasLast(final List<E> boards, final long size) {
        return boards.size() <= size;
    }

    private List<E> removeLastIfOverloaded(final List<E> boards) {
        LinkedList<E> boardSelectResponses = new LinkedList<>(boards);

        if (!isLast) {
            boardSelectResponses.removeLast();
        }

        return Collections.unmodifiableList(boardSelectResponses);
    }
}
