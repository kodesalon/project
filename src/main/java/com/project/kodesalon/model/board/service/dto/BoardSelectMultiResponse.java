package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSelectMultiResponse {
    private List<BoardSelectSingleResponse> boards;

    public BoardSelectMultiResponse(final List<BoardSelectSingleResponse> boards) {
        this.boards = boards;
    }
}
