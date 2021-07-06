package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.board.domain.vo.Content;
import com.project.kodesalon.model.board.domain.vo.Title;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static com.project.kodesalon.model.member.domain.MemberTest.TEST_MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    @DisplayName("게시판 객체를 DB에 저장한다.")
    public void save() {
        testEntityManager.persist(TEST_MEMBER);
        Board board = new Board(new Title("게시물 제목"), new Content("게시물 내용"), TEST_MEMBER, LocalDateTime.now());
        board = boardRepository.save(board);
        assertThat(board.getId()).isNotNull();
    }
}
