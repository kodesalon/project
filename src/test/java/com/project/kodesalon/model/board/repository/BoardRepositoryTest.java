package com.project.kodesalon.model.board.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.project.kodesalon.config.DBUnitTestConfiguration;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@ActiveProfiles(profiles = "test")
@Import(DBUnitTestConfiguration.class)
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup(value = "classpath:boardRepositoryTestDataSet.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:boardRepositoryTestDataSet.xml", type = DatabaseOperation.DELETE_ALL)
@TestExecutionListeners({DbUnitTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    @DisplayName("회원 식별 번호를 입력받으면 해당 작성 식별 번호를 가진 게시물의 deleted를 true로 변환한다.")
    void deleteBoardByMemberId() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        boardRepository.deleteBoardByMemberId(1L);

        softly.then(boardRepository.findById(1L)).isEmpty();
        softly.then(boardRepository.findById(3L)).isEmpty();
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
        boards.forEach(board -> softly.then(persistenceUnitUtil.isLoaded(board.getWriter())).isTrue());
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
