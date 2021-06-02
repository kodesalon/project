package com.project.kodesalon.model.board.service.dto;

import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequestDto {
    private Long memberId;
    private Title title;
    private Content content;
    private String createdDateTime;

    public BoardCreateRequestDto(Long memberId, String title, String content, String createdDateTime) {
        this.memberId = memberId;
        this.title = new Title(title);
        this.content = new Content(content);
        this.createdDateTime = createdDateTime;
    }
}
