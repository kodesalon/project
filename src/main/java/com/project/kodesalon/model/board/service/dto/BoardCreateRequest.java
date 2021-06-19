package com.project.kodesalon.model.board.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import com.project.kodesalon.model.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.common.ErrorCode.INVALID_MEMBER_ID;
import static com.project.kodesalon.model.board.domain.vo.Content.CONTENT_LENGTH_BOUND_MAX;
import static com.project.kodesalon.model.board.domain.vo.Title.TITLE_LENGTH_MAX_BOUND;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequest {

    @NotNull(message = INVALID_MEMBER_ID)
    private Long memberId;

    @NotEmpty(message = INVALID_BOARD_TITLE)
    @Length(max = TITLE_LENGTH_MAX_BOUND, message = INVALID_BOARD_TITLE)
    private String title;

    @NotEmpty(message = INVALID_BOARD_CONTENT)
    @Length(max = CONTENT_LENGTH_BOUND_MAX, message = INVALID_BOARD_CONTENT)
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public BoardCreateRequest(final Long memberId, final String title, final String content, final LocalDateTime createdDateTime) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
    }

    public Board toBoard(final Member writer) {
        Title title = new Title(this.title);
        Content content = new Content(this.content);
        return new Board(title, content, writer, createdDateTime);
    }
}

