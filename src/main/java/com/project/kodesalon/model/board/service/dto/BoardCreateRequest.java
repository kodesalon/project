package com.project.kodesalon.model.board.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.project.kodesalon.model.board.domain.vo.Content.CHECK_CONTENT_IS_BLANK;
import static com.project.kodesalon.model.board.domain.vo.Content.CHECK_CONTENT_LENGTH;
import static com.project.kodesalon.model.board.domain.vo.Content.CONTENT_LENGTH_BOUND_MAX;
import static com.project.kodesalon.model.board.domain.vo.Title.CHECK_TITLE_IS_BLANK;
import static com.project.kodesalon.model.board.domain.vo.Title.CHECK_TITLE_LENGTH;
import static com.project.kodesalon.model.board.domain.vo.Title.TITLE_LENGTH_MAX_BOUND;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequest {

    @NotNull(message = "null이 아닌 회원 식별 번호를 입력해주세요.")
    private Long memberId;

    @NotEmpty(message = CHECK_TITLE_IS_BLANK)
    @Length(max = TITLE_LENGTH_MAX_BOUND, message = CHECK_TITLE_LENGTH)
    private String title;

    @NotEmpty(message = CHECK_CONTENT_IS_BLANK)
    @Length(max = CONTENT_LENGTH_BOUND_MAX, message = CHECK_CONTENT_LENGTH)
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public BoardCreateRequest(Long memberId, String title, String content, LocalDateTime createdDateTime) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
    }

    public Board toBoard(String writer) {
        return new Board(new Title(title), new Content(content), writer, createdDateTime);
    }
}

