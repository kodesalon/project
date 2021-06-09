package com.project.kodesalon.model.member.repository;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

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
                .orElseThrow(() -> HttpClientErrorException.create("존재하는 아이디를 입력해주세요.", HttpStatus.UNAUTHORIZED,
                        "", HttpHeaders.EMPTY, null, null));
        BDDSoftAssertions softly = new BDDSoftAssertions();

        softly.then(savedMember.getAlias()).isEqualTo(member.getAlias());
        softly.then(savedMember.getPassword()).isEqualTo(member.getPassword());
        softly.then(savedMember.getName()).isEqualTo(member.getName());
        softly.then(savedMember.getEmail()).isEqualTo(member.getEmail());
        softly.then(savedMember.getPhone()).isEqualTo(member.getPhone());

        softly.assertAll();
    }

    @Test
    @DisplayName("DB에 존재하지 않는 멤버를 존재하면 Optional.empty를 리턴합니다.")
    void find_by_alias_not_present_member_return_optional_empty() {
        Optional<Member> notPresentMember = memberRepository.findMemberByAlias(new Alias("notAlias"));

        then(notPresentMember).isEmpty();
    }

    @Test
    @DisplayName("비밀번호를 변경한다.")
    public void updatePassword() {
        member.changePassword("ChangePassword1!");
        testEntityManager.flush();
        testEntityManager.clear();
        Member foundMember = memberRepository.findById(member.getId()).get();
        then(foundMember.getPassword()).isEqualTo("ChangePassword1!");
    }

    @Test
    @DisplayName("회원을 탈퇴합니다")
    void delete_member() {
        Member deleteMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new NoSuchElementException("삭제하려는 회원이 없습니다"));

        memberRepository.delete(deleteMember);
        testEntityManager.flush();
        testEntityManager.clear();

        then(memberRepository.findById(deleteMember.getId())).isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("회원 탈퇴2")
    void delete() {
        memberRepository.deleteById(member.getId());

        testEntityManager.flush();
        testEntityManager.clear();

        then(memberRepository.findById(member.getId())).isEmpty();
    }
}
