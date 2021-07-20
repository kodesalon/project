package com.project.kodesalon.model.board.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.project.kodesalon.common.ErrorCode.INVALID_BOARD_ID;
import static com.project.kodesalon.common.ErrorCode.INVALID_DATE_TIME;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDeleteRequest {

    @NotNull(message = INVALID_BOARD_ID)
    private Long boardId;

    @NotNull(message = INVALID_DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime deletedDateTime;

    public BoardDeleteRequest(final Long boardId, final LocalDateTime deletedDateTime) {
        this.boardId = boardId;
        this.deletedDateTime = deletedDateTime;
    }
}
