package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultiBoardSelectResponse {
    private List<BoardSelectResponse> boards;
    private boolean hasNext;

    public MultiBoardSelectResponse(final List<BoardSelectResponse> boards) {
        this.boards = boards;
        this.hasNext = hasNextBoard(lastBoard());
    }

    private BoardSelectResponse lastBoard() {
        return boards.get(boards.size() - 1);
    }

    private boolean hasNextBoard(final BoardSelectResponse boardSelectResponse) {
        return !boardSelectResponse.checkLast();
    }
}
