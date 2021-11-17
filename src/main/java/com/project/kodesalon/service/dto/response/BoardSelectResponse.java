package com.project.kodesalon.service.dto.response;

import com.project.kodesalon.service.dto.BoardQueryDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardSelectResponse {

    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdDateTime;
    private Long writerId;
    private String writerAlias;
    private List<BoardImageResponse> boardImages;

    public BoardSelectResponse(final Long boardId, final String title, final String content, final LocalDateTime createdDateTime, final Long writerId, String writerAlias, final List<BoardImageResponse> boardImages) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.writerId = writerId;
        this.writerAlias = writerAlias;
        this.boardImages = boardImages;
    }

    public BoardSelectResponse(final BoardQueryDto boardQueryDto, final List<BoardImageResponse> boardImageResponses) {
        this.boardId = boardQueryDto.getBoardId();
        this.title = boardQueryDto.getTitle();
        this.content = boardQueryDto.getContent();
        this.createdDateTime = boardQueryDto.getCreatedDateTime();
        this.writerId = boardQueryDto.getMemberId();
        this.writerAlias = boardQueryDto.getMemberAlias();
        this.boardImages = boardImageResponses;
    }
}
