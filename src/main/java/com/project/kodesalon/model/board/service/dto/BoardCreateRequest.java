package com.project.kodesalon.model.board.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.member.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.model.board.domain.vo.Content.CONTENT_LENGTH_BOUND_MAX;
import static com.project.kodesalon.model.board.domain.vo.Title.TITLE_LENGTH_MAX_BOUND;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequest {

    @NotEmpty(message = INVALID_BOARD_TITLE)
    @Length(max = TITLE_LENGTH_MAX_BOUND, message = INVALID_BOARD_TITLE)
    private String title;

    @NotEmpty(message = INVALID_BOARD_CONTENT)
    @Length(max = CONTENT_LENGTH_BOUND_MAX, message = INVALID_BOARD_CONTENT)
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public BoardCreateRequest(String title, String content, LocalDateTime createdDateTime) {
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
    }

    public Board toBoard(Member writer) {
        return new Board(title, content, writer, createdDateTime);
    }
}
