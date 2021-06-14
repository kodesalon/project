package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("게시판 객체를 DB에 저장한다.")
    public void save() {
        Board board = new Board(new Title("게시물 제목"), new Content("게시물 내용"), "작성자", LocalDateTime.now());
        board = boardRepository.save(board);
        assertThat(board.getId()).isNotNull();
    }
}