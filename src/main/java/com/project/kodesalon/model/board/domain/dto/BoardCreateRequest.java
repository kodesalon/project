package com.project.kodesalon.model.board.domain.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequest {
    private Long memberId;
    private String title;
    private String content;
    private String createdDateTime;

    public BoardCreateRequest(Long memberId, String title, String content, String createdDateTime) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
    }
}
