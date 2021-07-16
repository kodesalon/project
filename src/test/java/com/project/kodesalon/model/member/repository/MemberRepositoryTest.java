package com.project.kodesalon.model.member.repository;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("alias", "Password!!123", "이름", "email@email.com", "010-1234-4444");
        memberRepository.save(member);
    }

    @Test
    @DisplayName("DB에 존재하는 회원을 아이디(Alias)로 조회할 경우, 해당 아이디를 가진 회원을 리턴합니다")
    void findMemberByAlias() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        Member savedMember = memberRepository.findMemberByAlias(new Alias("alias"))
                .orElseThrow(() -> new NoSuchElementException("존재하는 아이디를 입력해주세요."));

        softly.then(savedMember.getAlias()).isEqualTo(member.getAlias());
        softly.then(savedMember.getPassword()).isEqualTo(member.getPassword());
        softly.then(savedMember.getName()).isEqualTo(member.getName());
        softly.then(savedMember.getEmail()).isEqualTo(member.getEmail());
        softly.then(savedMember.getPhone()).isEqualTo(member.getPhone());
        softly.assertAll();
    }

    @Test
    @DisplayName("DB에 존재하지 않는 회원을 아이디(Alias)로 조회할 경우, Optional.empty를 리턴합니다.")
    void findMemberByAlias_with_invalid_alias() {
        Optional<Member> notPresentMember = memberRepository.findMemberByAlias(new Alias("notAlias"));

        then(notPresentMember).isEmpty();
    }
}
