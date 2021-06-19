package com.project.kodesalon.model.board.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.model.board.domain.vo.Content.CONTENT_LENGTH_BOUND_MAX;
import static com.project.kodesalon.model.board.domain.vo.Title.TITLE_LENGTH_MAX_BOUND;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class BoardUpdateRequest {

    @NotNull(message = "null이 아닌 회원 식별 번호를 입력해주세요.")
    private Long memberId;

    @NotEmpty(message = INVALID_BOARD_TITLE)
    @Length(max = TITLE_LENGTH_MAX_BOUND, message = INVALID_BOARD_TITLE)
    private String updatedTitle;

    @NotEmpty(message = INVALID_BOARD_CONTENT)
    @Length(max = CONTENT_LENGTH_BOUND_MAX, message = INVALID_BOARD_CONTENT)
    private String updatedContent;

    public BoardUpdateRequest(Long memberId, String updatedTitle, String updatedContent) {
        this.memberId = memberId;
        this.updatedTitle = updatedTitle;
        this.updatedContent = updatedContent;
    }
}
