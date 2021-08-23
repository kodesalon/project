package com.project.kodesalon.repository.member;

import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.domain.member.vo.Alias;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.project.kodesalon.exception.ErrorCode.NOT_EXIST_MEMBER_ALIAS;
import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class MemberRepositoryTest {

    private static final Member member = new Member("alias", "Password!!123", "이름", "email@email.com",
            "010-1234-4444", LocalDateTime.of(2021, 7, 16, 23, 59));
    private final BDDSoftAssertions softly = new BDDSoftAssertions();

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(member);
    }

    @Test
    @DisplayName("DB에 존재하는 회원을 아이디(Alias)로 조회할 경우, 해당 아이디를 가진 회원을 리턴합니다")
    void findMemberByAlias() {
        Member savedMember = memberRepository.findMemberByAlias(new Alias("alias"))
                .orElseThrow(() -> new EntityNotFoundException(NOT_EXIST_MEMBER_ALIAS));

        softly.then(savedMember.getAlias()).isEqualTo(member.getAlias());
        softly.then(savedMember.getPassword()).isEqualTo(member.getPassword());
        softly.then(savedMember.getName()).isEqualTo(member.getName());
        softly.then(savedMember.getEmail()).isEqualTo(member.getEmail());
        softly.then(savedMember.getPhone()).isEqualTo(member.getPhone());
        softly.then(savedMember.getCreatedDateTime()).isEqualTo(member.getCreatedDateTime());
        softly.assertAll();
    }

    @Test
    @DisplayName("DB에 존재하지 않는 회원을 아이디(Alias)로 조회할 경우, Optional.empty를 리턴합니다.")
    void findMemberByAlias_with_invalid_alias() {
        Optional<Member> notPresentMember = memberRepository.findMemberByAlias(new Alias("notAlias"));

        then(notPresentMember).isEmpty();
    }
}
