package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSelectResponse {

    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdDateTime;
    private Long writerId;
    private String writerAlias;

    public BoardSelectResponse(final Long boardId, final String title, final String content, final LocalDateTime createdDateTime, final Long writerId, String writerAlias) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.writerId = writerId;
        this.writerAlias = writerAlias;
    }

    public boolean checkLast() {
        return this.getBoardId() == 0L;
    }
}
