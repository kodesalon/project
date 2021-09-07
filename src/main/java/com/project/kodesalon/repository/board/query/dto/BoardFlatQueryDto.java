package com.project.kodesalon.repository.board.query.dto;

import com.project.kodesalon.domain.board.vo.Content;
import com.project.kodesalon.domain.board.vo.Title;
import com.project.kodesalon.domain.member.vo.Alias;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode(of = "boardId")
public class BoardFlatQueryDto {
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime createdDateTime;
    private Long writerId;
    private String writerAlias;
    private Long imageId;
    private String imageUrl;

    public BoardFlatQueryDto(final Long boardId, final Title title, final Content content,
                             final LocalDateTime createdDateTime, final Long writerId,
                             final Alias writerAlias, final Long imageId, final String imageUrl) {
        this.boardId = boardId;
        this.title = title.value();
        this.content = content.value();
        this.createdDateTime = createdDateTime;
        this.writerId = writerId;
        this.writerAlias = writerAlias.value();
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }
}
