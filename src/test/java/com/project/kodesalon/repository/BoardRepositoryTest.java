package com.project.kodesalon.repository;

import com.project.kodesalon.domain.Board;
import com.project.kodesalon.domain.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private PersistenceUnitUtil persistenceUnitUtil;

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    private Member member;
    private Board board;

    @BeforeEach
    void setUp() {
        member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444", LocalDateTime.now());
        testEntityManager.persist(member);
        board = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        board = boardRepository.save(board);
        testEntityManager.flush();
        testEntityManager.clear();
        persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
    }

    @Test
    @DisplayName("게시판 객체를 DB에 저장한다.")
    void save() {
        then(board.getId()).isNotNull();
    }

    @Test
    @DisplayName("회원 식별 번호를 입력받으면 해당 작성 식별 번호를 가진 게시물의 deleted를 true로 변환한다.")
    void deleteBoardByMemberId() {
        Board firstBoard = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        Board secondBoard = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        boardRepository.save(firstBoard);
        boardRepository.save(secondBoard);

        boardRepository.deleteBoardByMemberId(member.getId());
        testEntityManager.clear();

        softly.then(boardRepository.findById(firstBoard.getId())).isEmpty();
        softly.then(boardRepository.findById(secondBoard.getId())).isEmpty();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 식별 번호를 입력받아 게시물을 조회하면 작성자 정보와 함께 조인하여 반환한다.")
    void selectBoardById() {
        Optional<Board> board = boardRepository.selectBoardById(this.board.getId());

        softly.then(board).isNotEmpty();
        softly.then(persistenceUnitUtil.isLoaded(board.get().getWriter())).isTrue();
    }

    @Test
    @DisplayName("마지막 게시물 번호를 입력받아 이후의 10개의 게시물과 작성자의 정보를 조인하여 반환한다.")
    void findTop10Boards() {
        int boardToBeSelectedAtOnce = 10;

        for (int board_number = 0; board_number < boardToBeSelectedAtOnce + 1; board_number++) {
            Board board = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
            boardRepository.save(board);
        }

        List<Board> boards = boardRepository.selectBoards(1L, boardToBeSelectedAtOnce);

        softly.then(boards.size()).isEqualTo(boardToBeSelectedAtOnce);
        boards.forEach(b -> {
            softly.then(persistenceUnitUtil.isLoaded(b.getWriter())).isTrue();
        });
        softly.assertAll();
    }
}
