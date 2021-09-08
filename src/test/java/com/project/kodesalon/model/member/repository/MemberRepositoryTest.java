package com.project.kodesalon.model.member.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.project.kodesalon.config.DBUnitTestConfiguration;
import com.project.kodesalon.model.board.domain.Board;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.project.kodesalon.common.ErrorCode.NOT_EXIST_MEMBER_ALIAS;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
@ActiveProfiles(profiles = "test")
@Import(DBUnitTestConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DbUnitConfiguration(databaseConnection = "dbUnitDatabaseConnection")
@DatabaseSetup(value = "classpath:memberRepositoryDataSet.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:memberRepositoryDataSet.xml", type = DatabaseOperation.DELETE_ALL)
@TestExecutionListeners({DbUnitTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class MemberRepositoryTest {

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("DB에 존재하는 회원을 아이디(Alias)로 조회할 경우, 해당 아이디를 가진 회원을 리턴합니다")
    void findMemberByAlias() {
        Member savedMember = memberRepository.findMemberByAlias(new Alias("alias1"))
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_MEMBER_ALIAS));

        softly.then(savedMember.getId()).isEqualTo(1L);
        softly.then(savedMember.getAlias()).isEqualTo("alias1");
        softly.then(savedMember.getPassword()).isEqualTo("Password123!");
        softly.then(savedMember.getName()).isEqualTo("이름");
        softly.then(savedMember.getEmail()).isEqualTo("email@email.com");
        softly.then(savedMember.getPhone()).isEqualTo("010-1111-2222");
        softly.then(savedMember.getCreatedDateTime()).isEqualTo(LocalDateTime.of(2021, 7, 18, 17, 48, 22));
        softly.assertAll();
    }

    @Test
    @DisplayName("DB에 존재하지 않는 회원을 아이디(Alias)로 조회할 경우, Optional.empty를 리턴합니다.")
    void findMemberByAlias_with_invalid_alias() {
        Optional<Member> notPresentMember = memberRepository.findMemberByAlias(new Alias("notAlias"));

        then(notPresentMember).isEmpty();
    }

    @Test
    @DisplayName("회원 식별 번호를 입력받아 회원 정보와 회원이 올린 게시물 정보와 조인하여 반환한다")
    void findMemberById() {
        int size = 5;
        Member member
                = new Member("alias1", "Password123!", "이름", "email@email.com", "010-1111-2222", LocalDateTime.now());
        memberRepository.save(member);
        for (int i = 0; i < size; i++) {
            Board myBoard = new Board("게시물 제목", "게시물 내용", member, LocalDateTime.now());
            entityManager.persist(myBoard);
        }
        entityManager.flush();
        entityManager.clear();

        Member selectMember = memberRepository.selectMemberById(member.getId()).get();

        List<Board> boards = selectMember.getBoards();

        boards.forEach(board -> softly.then(persistenceUnitUtil.isLoaded(board)).isTrue());
        softly.assertAll();
    }
}
