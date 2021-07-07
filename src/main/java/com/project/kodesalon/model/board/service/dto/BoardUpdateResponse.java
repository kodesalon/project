package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BoardUpdateResponse {
    private String message;

    public BoardUpdateResponse(String message) {
        this.message = message;
    }
}
