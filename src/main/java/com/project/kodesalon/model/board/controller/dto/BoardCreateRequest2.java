package com.project.kodesalon.model.board.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateRequest2 {
    private Long memberId;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDateTime;

    public BoardCreateRequest2(Long memberId, String title, String content, LocalDateTime createdDateTime) {
        this.memberId = memberId;
        this.title = title;
        this.content = content;
        this.createdDateTime = createdDtTme;
    }
}



