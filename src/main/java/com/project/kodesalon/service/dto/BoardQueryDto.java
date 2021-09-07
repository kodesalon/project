package com.project.kodesalon.service.dto;

import com.project.kodesalon.repository.board.query.dto.BoardFlatQueryDto;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = "boardId")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardQueryDto {

    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdDateTime;
    private Long memberId;
    private String memberAlias;

    public BoardQueryDto(final BoardFlatQueryDto flat) {
        this.boardId = flat.getBoardId();
        this.title = flat.getTitle();
        this.content = flat.getContent();
        this.createdDateTime = flat.getCreatedDateTime();
        this.memberId = flat.getWriterId();
        this.memberAlias = flat.getWriterAlias();
    }
}

