package com.project.kodesalon.domain.image;

import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.domain.member.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static com.project.kodesalon.exception.ErrorCode.INVALID_BOARD_IMAGES_SIZE;
import static org.assertj.core.api.BDDAssertions.thenIllegalArgumentException;

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
        softly.then(image.getBoard()).isNotNull();
        softly.then(board.getImages()).hasSize(1);
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 하나에 6개 이상의 이미지가 생성될 경우 예외가 발생한다.")
    void create_throws_exception_with_invalid_board_size() {
        Member member = new Member("alias", "Password123!", "이름", "id@domain.com", "010-0000-0000", LocalDateTime.now());
        Board board = new Board("제목", "내용", member, LocalDateTime.now());
        String key = "image/file.png";
        String url = "localhost:8080/bucket/" + key;
        IntStream.range(0, 5)
                .forEach(time -> new Image(url, board));

        thenIllegalArgumentException().isThrownBy(() -> new Image(url, board))
                .withMessage(INVALID_BOARD_IMAGES_SIZE);
    }
}
