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
}
