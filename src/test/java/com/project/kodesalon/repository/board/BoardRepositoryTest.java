package com.project.kodesalon.repository.board;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.project.kodesalon.config.dbunit.annotation.DbUnitTest;
import com.project.kodesalon.domain.board.Board;
import com.project.kodesalon.repository.board.query.dto.BoardFlatQueryDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DbUnitTest
@DatabaseSetup(value = "classpath:boardRepositoryTestDataSet.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:boardRepositoryTestDataSet.xml", type = DatabaseOperation.DELETE_ALL)
class BoardRepositoryTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private BoardRepository boardRepository;

    private PersistenceUnitUtil persistenceUnitUtil;

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @BeforeEach
    void setUp() {
        persistenceUnitUtil = entityManagerFactory.getPersistenceUnitUtil();
    }

    @Test
    @DisplayName("회원 식별 번호를 입력받으면 해당 작성 식별 번호를 가진 게시물의 deleted를 true로 변환한다.")
    void deleteBoardByMemberId() {
        boardRepository.deleteBoardByMemberId(1L);

        softly.then(boardRepository.findById(1L)).isEmpty();
        softly.then(boardRepository.findById(3L)).isEmpty();
        softly.assertAll();
    }

    @Test
    @DisplayName("게시물 식별 번호를 입력받아 게시물을 조회하면 작성자 정보와 함께 조인하여 반환한다.")
    void selectBoard() {
        Optional<Board> selectedBoard = boardRepository.selectBoardById(1L);

        softly.then(selectedBoard).isNotEmpty();
        softly.then(persistenceUnitUtil.isLoaded(selectedBoard.get().getWriter())).isTrue();
    }

    @Test
    @DisplayName("마지막으로 조회한 게시물 번호, 조회할 게시물 수를 입력받아 다음 게시물이 존재할 경우 입력 크기보다 하나 많은 게시물과 입력 작성자의 정보를 조인하여 반환한다.")
    void selectBoards_has_next() {
        int boardToBeSelectedAtOnce = 10;

        List<BoardFlatQueryDto> boards = boardRepository.selectQueryBoards(11L, boardToBeSelectedAtOnce);

        then(boards.size()).isEqualTo(boardToBeSelectedAtOnce);
    }

    @Test
    @DisplayName("마지막으로 조회한 게시물 번호, 조회할 게시물 수를 입력받아 다음이 게시물이 존재하지 않을 경우 조회한 게시물과 이미지를 조인하여 반환한다.")
    void selectBoards_doesnt_have_next() {
        int boardToBeSelectedAtOnce = 10;

        List<BoardFlatQueryDto> boards = boardRepository.selectQueryBoards(Long.MAX_VALUE - 1, boardToBeSelectedAtOnce);

        then(boards.size()).isEqualTo(9);
    }

    @Test
    @DisplayName("회원 식별 번호, 마지막으로 조회한 게시물 번호, 조회할 게시물 수를 입력받아 다음으로 회원이 올린 게시물이 존재할 경우 입력 크기보다 하나 많은 게시물과 이미지를 조인하여 반환한다.")
    void selectMyBoards_has_next() {
        int boardToBeSelectedAtOnce = 10;

        List<Board> boards = boardRepository.selectMyBoards(1L, 11L, boardToBeSelectedAtOnce);

        softly.then(boards.size()).isEqualTo(boardToBeSelectedAtOnce);
        boards.forEach(board -> softly.then(persistenceUnitUtil.isLoaded(board.getImages())).isTrue());
        softly.assertAll();
    }

    @Test
    @DisplayName("회원 식별 번호, 마지막으로 조회한 게시물 번호, 조회할 게시물 수를 입력받아 다음으로 회원이 올린 게시물이 존재하지 않을 경우 조회한 게시물과 이미지를 조인하여 반환한다.")
    void selectMyBoards_doesnt_have_next() {
        int boardToBeSelectedAtOnce = 10;

        List<Board> boards = boardRepository.selectMyBoards(1L, 10L, boardToBeSelectedAtOnce);

        softly.then(boards.size()).isEqualTo(boardToBeSelectedAtOnce - 1);
        boards.forEach(board -> softly.then(persistenceUnitUtil.isLoaded(board.getImages())).isTrue());
        softly.assertAll();
    }
}
