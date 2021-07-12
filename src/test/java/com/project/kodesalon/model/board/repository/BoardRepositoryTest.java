package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.member.domain.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444");
        testEntityManager.persist(member);
    }

    @Test
    @DisplayName("게시판 객체를 DB에 저장한다.")
    void save() {
        Board board = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        board = boardRepository.save(board);
        then(board.getId()).isNotNull();
    }

    @Test
    @DisplayName("회원 식별 번호를 입력받으면 해당 작성 식별 번호를 가진 게시물의 deleted를 true로 변환한다.")
    void deleteBoardByMemberId() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        Board firstBoard = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        Board secondBoard = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        boardRepository.save(firstBoard);
        boardRepository.save(secondBoard);

        boardRepository.deleteBoardByMemberId(member.getId());

        softly.then(boardRepository.findById(firstBoard.getId())).isEmpty();
        softly.then(boardRepository.findById(secondBoard.getId())).isEmpty();
        softly.assertAll();
    }
}
