package com.project.kodesalon.service.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberOwnBoardSelectResponse {
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdDateTime;

    public MemberOwnBoardSelectResponse(final Long boardId, final String title, final String content, final LocalDateTime createdDateTime) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
    }
}
