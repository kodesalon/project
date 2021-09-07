package com.project.kodesalon.repository.board.query.dto;

import com.project.kodesalon.domain.board.vo.Content;
import com.project.kodesalon.domain.board.vo.Title;
import com.project.kodesalon.domain.member.vo.Alias;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class BoardFlatQueryDtoTest {

    @Test
    @DisplayName("")
    void create() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        BoardFlatQueryDto boardFlatQueryDto = new BoardFlatQueryDto(1L, new Title("게시물 제목"), new Content("게시물 내용"),
                LocalDateTime.of(2021, 8, 31, 9, 20, 21), 1L,
                new Alias("alias"), 1L, "localhost:8080/bucket/image.png");

        softly.then(boardFlatQueryDto.getBoardId()).isEqualTo(1L);
        softly.then(boardFlatQueryDto.getTitle()).isEqualTo("게시물 제목");
        softly.then(boardFlatQueryDto.getContent()).isEqualTo("게시물 내용");
        softly.then(boardFlatQueryDto.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2021, 8, 31, 9, 20, 21));
        softly.then(boardFlatQueryDto.getWriterId()).isEqualTo(1L);
        softly.then(boardFlatQueryDto.getWriterAlias()).isEqualTo("alias");
        softly.then(boardFlatQueryDto.getImageId()).isEqualTo(1L);
        softly.then(boardFlatQueryDto.getImageUrl()).isEqualTo("localhost:8080/bucket/image.png");
        softly.assertAll();
    }
}
