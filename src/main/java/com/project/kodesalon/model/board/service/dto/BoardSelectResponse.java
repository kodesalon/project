package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSelectResponse {
    private String title;
    private String content;
    private String createdDateTime;
    private String writer;

    public BoardSelectResponse(final String title, final String content, final String createdDateTime, final String writer) {
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.writer = writer;
    }
}
