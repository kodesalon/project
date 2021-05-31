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
}
