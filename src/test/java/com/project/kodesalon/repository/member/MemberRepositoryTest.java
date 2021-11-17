package com.project.kodesalon.repository.member;

import com.project.kodesalon.config.QuerydslTestConfiguration;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.project.kodesalon.config.dbunit.annotation.DbUnitTest;
import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.domain.member.vo.Alias;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER_ALIAS;
import static org.assertj.core.api.BDDAssertions.then;

@DbUnitTest
@DatabaseSetup(value = "classpath:memberRepositoryDataSet.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:memberRepositoryDataSet.xml", type = DatabaseOperation.DELETE_ALL)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private final BDDSoftAssertions softly = new BDDSoftAssertions();

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
}
