package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSelectResponse {
    private Long boardId;
    private String title;
    private String content;
    private String createdDateTime;
    private Long writerId;
    private String writerAlias;

    public BoardSelectResponse(final Long boardId, final String title, final String content, final String createdDateTime, final Long writerId, String writerAlias) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.writerId = writerId;
        this.writerAlias = writerAlias;
    }
}
