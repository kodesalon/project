package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.project.kodesalon.model.board.domain.BoardTest.TEST_BOARD;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시판 객체를 DB에 저장한다.")
    public void save() {
        Board savedBoard = boardRepository.save(TEST_BOARD);
        assertThat(TEST_BOARD.getId()).isEqualTo(savedBoard.getId());
    }
}
