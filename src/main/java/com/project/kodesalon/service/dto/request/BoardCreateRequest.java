package com.project.kodesalon.service.dto.request;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.project.kodesalon.domain.board.Board.BOARD_IMAGE_LENGTH_MAX_BOUND;
import static com.project.kodesalon.domain.board.vo.Content.CONTENT_LENGTH_MAX_BOUND;
import static com.project.kodesalon.domain.board.vo.Title.TITLE_LENGTH_MAX_BOUND;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_CONTENT;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_IMAGES_SIZE;
import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_TITLE;
import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequest {

    @NotEmpty(message = INVALID_BOARD_TITLE)
    @Length(max = TITLE_LENGTH_MAX_BOUND, message = INVALID_BOARD_TITLE)
    private String title;

    @NotEmpty(message = INVALID_BOARD_CONTENT)
    @Length(max = CONTENT_LENGTH_MAX_BOUND, message = INVALID_BOARD_CONTENT)
    private String content;

    @NotNull(message = INVALID_DATE_TIME)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDateTime;

    @Size(max = BOARD_IMAGE_LENGTH_MAX_BOUND, message = INVALID_BOARD_IMAGES_SIZE)
    private List<MultipartFile> images;

    public BoardCreateRequest(final String title, final String content, final LocalDateTime createdDateTime, final Optional<List<MultipartFile>> images) {
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDateTime;
        this.images = images.orElseGet(ArrayList::new);
    }

    public Board toBoard(Member writer) {
        return new Board(title, content, writer, createdDateTime);
    }
}

