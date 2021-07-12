package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.model.board.domain.vo.Content.CONTENT_LENGTH_MAX_BOUND;
import static com.project.kodesalon.model.board.domain.vo.Title.TITLE_LENGTH_MAX_BOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BoardUpdateRequest {

    @NotEmpty(message = INVALID_BOARD_TITLE)
    @Length(max = TITLE_LENGTH_MAX_BOUND, message = INVALID_BOARD_TITLE)
    private String updatedTitle;

    @NotEmpty(message = INVALID_BOARD_CONTENT)
    @Length(max = CONTENT_LENGTH_MAX_BOUND, message = INVALID_BOARD_CONTENT)
    private String updatedContent;

    public BoardUpdateRequest(String updatedTitle, String updatedContent) {
        this.updatedTitle = updatedTitle;
        this.updatedContent = updatedContent;
    }
}
