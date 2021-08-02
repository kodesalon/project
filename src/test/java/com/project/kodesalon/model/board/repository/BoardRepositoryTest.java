package com.project.kodesalon.model.board.repository;

import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.member.domain.Member;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    private PersistenceUnitUtil persistenceUnitUtil;

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @BeforeEach
    void setUp() {
        persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
    }

    @AfterEach
    void tearDown() {
        boardRepository.deleteAll();
        entityManager.createNativeQuery("ALTER TABLE board ALTER COLUMN `board_id` RESTART WITH 1")
                .executeUpdate();
    }

    @Test
    @DisplayName("게시판 객체를 DB에 저장한다.")
    void save() {
        Member member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444", LocalDateTime.now());
        entityManager.persist(member);
        Board board = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());

        board = boardRepository.save(board);

        then(board.getId()).isNotNull();
    }


    @Test
    @DisplayName("회원 식별 번호를 입력받으면 해당 작성 식별 번호를 가진 게시물의 deleted를 true로 변환한다.")
    void deleteBoardByMemberId() {
        Member member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444", LocalDateTime.now());
        entityManager.persist(member);
        Board firstBoard = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        Board secondBoard = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        boardRepository.save(firstBoard);
        boardRepository.save(secondBoard);

        boardRepository.deleteBoardByMemberId(member.getId());
        entityManager.flush();
        entityManager.clear();

        softly.then(boardRepository.findById(firstBoard.getId())).isEmpty();
        softly.then(boardRepository.findById(secondBoard.getId())).isEmpty();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 식별 번호를 입력받아 게시물을 조회하면 작성자 정보와 함께 조인하여 반환한다.")
    void selectBoard() {
        Member member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444", LocalDateTime.now());
        entityManager.persist(member);
        Board board = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
        boardRepository.save(board);
        entityManager.flush();
        entityManager.clear();

        Optional<Board> selectedBoard = boardRepository.selectBoardById(board.getId());

        softly.then(selectedBoard).isNotEmpty();
        softly.then(persistenceUnitUtil.isLoaded(selectedBoard.get().getWriter())).isTrue();
    }

    @Test
    @DisplayName("마지막으로 조회한 게시물 번호, 조회할 게시물 수를 입력받아 다음 게시물이 존재할 경우 입력 크기보다 하나 많은 게시물과 입력 작성자의 정보를 조인하여 반환한다.")
    void selectBoards_has_next() {
        Member member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444", LocalDateTime.now());
        entityManager.persist(member);
        int boardToBeSelectedAtOnce = 10;
        for (int board_number = 0; board_number <= boardToBeSelectedAtOnce; board_number++) {
            Board board = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
            boardRepository.save(board);
        }
        entityManager.flush();
        entityManager.clear();

        List<Board> boards = boardRepository.selectBoards(11L, boardToBeSelectedAtOnce);

        softly.then(boards.size()).isEqualTo(boardToBeSelectedAtOnce);
        boards.forEach(b -> {
            softly.then(persistenceUnitUtil.isLoaded(b.getWriter())).isTrue();
        });
        softly.assertAll();
    }

    @Test
    @DisplayName("마지막으로 조회한 게시물 번호, 조회할 게시물 수를 입력받아 다음이 게시물이 존재하지 않을 경우 조회한 게시물과 작성자의 정보를 조인하여 반환한다.")
    void selectBoards_doesnt_have_next() {
        Member member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444", LocalDateTime.now());
        entityManager.persist(member);
        int boardToBeSelectedAtOnce = 10;
        for (int board_number = 0; board_number < boardToBeSelectedAtOnce - 1; board_number++) {
            Board board = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
            boardRepository.save(board);
        }
        entityManager.flush();
        entityManager.clear();

        List<Board> boards = boardRepository.selectBoards(Long.MAX_VALUE, boardToBeSelectedAtOnce);

        softly.then(boards.size()).isEqualTo(boardToBeSelectedAtOnce - 1);
        boards.forEach(board -> softly.then(persistenceUnitUtil.isLoaded(board.getWriter())).isTrue());
        softly.assertAll();
    }
}
