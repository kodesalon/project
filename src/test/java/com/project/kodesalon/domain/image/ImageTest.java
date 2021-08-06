package com.project.kodesalon.domain.image;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.member.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class ImageTest {

    @Test
    @DisplayName("이미지 저장 경로와 게시물 작성자를 인자로 받아 이미지 객체를 생성한다.")
    void create() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        Member member = new Member("alias", "Password123!", "이름", "id@domain.com", "010-0000-0000", LocalDateTime.now());
        Board board = new Board("제목", "내용", member, LocalDateTime.now());
        String key = "image/file.png";
        String url = "localhost:8080/bucket/" + key;

        Image image = new Image(url, board);

        softly.then(image.getUrl()).isEqualTo(url);
        softly.then(image.getKey()).isEqualTo(key);
        softly.then(board.getImages()).hasSize(1);
        softly.assertAll();
    }
}
