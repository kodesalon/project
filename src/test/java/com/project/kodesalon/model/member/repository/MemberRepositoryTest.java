package com.project.kodesalon.model.member.repository;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("alias", "Password1234!", "엄희상", "email@email.com", "010-1111-2222");
        memberRepository.save(member);
    }

    @Test
    @DisplayName("DB에 존재하는 member를 alias로 조회하면 member를 리턴합니다")
    void find_by_alias_present_member() {
        Member savedMember = memberRepository.findMemberByAlias(new Alias("alias"))
                .orElseThrow(() -> new UnAuthorizedException("존재하지 않는 사용자입니다"));

        assertAll(
                () -> then(savedMember.getAlias()).isEqualTo(member.getAlias()),
                () -> then(savedMember.getPassword()).isEqualTo(member.getPassword()),
                () -> then(savedMember.getName()).isEqualTo(member.getName()),
                () -> then(savedMember.getEmail()).isEqualTo(member.getEmail()),
                () -> then(savedMember.getPhone()).isEqualTo(member.getPhone())
        );
    }

    @Test
    @DisplayName("DB에 존재하지 않는 멤버를 존재하면 Optional.empty를 리턴합니다.")
    void find_by_alias_not_present_member_return_optional_empty() {
        Optional<Member> notPresentMember = memberRepository.findMemberByAlias(new Alias("notAlias"));

        then(notPresentMember).isEmpty();
    }

}
