package com.project.kodesalon.service.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.project.kodesalon.domain.board.vo.Content.CONTENT_LENGTH_MAX_BOUND;
import static com.project.kodesalon.domain.board.vo.Title.TITLE_LENGTH_MAX_BOUND;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardUpdateRequest {

    @NotEmpty(message = INVALID_BOARD_TITLE)
    @Length(max = TITLE_LENGTH_MAX_BOUND, message = INVALID_BOARD_TITLE)
    private String title;

    @NotEmpty(message = INVALID_BOARD_CONTENT)
    @Length(max = CONTENT_LENGTH_MAX_BOUND, message = INVALID_BOARD_CONTENT)
    private String content;

    @NotNull(message = INVALID_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime lastModifiedDateTime;

    public BoardUpdateRequest(String updatedTitle, String updatedContent, LocalDateTime lastModifiedDateTime) {
        this.title = updatedTitle;
        this.content = updatedContent;
        this.lastModifiedDateTime = lastModifiedDateTime;
    }
}
