package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final CreateMemberRequest createMemberRequest = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222");

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("회원가입이 성공하면 repository에 회원 객체를 저장합니다.")
    void join() {
        given(member.getId()).willReturn(1L);
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        memberService.join(createMemberRequest);

        verify(memberRepository, times(1)).findMemberByAlias(any(Alias.class));
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 아이디(Alias)일 경우, 예외 메세지를 반환합니다.")
    void join_throw_exception_with_already_exist() {
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.join(createMemberRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 아이디입니다");
    }

    @Test
    @DisplayName("회원 가입시 삭제한 회원이 다시 가입했을 경우에는 예외를 발생시킵니다")
    void join_throw_exception_with_left_alias_after_delete() {
        given(memberRepository.save(any(Member.class))).willThrow(new DataIntegrityViolationException("이미 삭제된 회원에 대한 Alias"));

        thenThrownBy(() -> memberService.join(createMemberRequest))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessage("이미 삭제된 회원에 대한 Alias");
    }

    @Test
    @DisplayName("회원정보 조회 성공 시, 회원 별명, 이름, 이메일, 전화 번호를 반환합니다.")
    void exist_id_response_member() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
        given(member.getAlias()).willReturn("alias");
        given(member.getName()).willReturn("이름");
        given(member.getEmail()).willReturn("email@email.com");
        given(member.getPhone()).willReturn("010-1111-2222");

        SelectMemberResponse selectMemberResponse = memberService.selectMember(anyLong());

        softly.then(selectMemberResponse.getAlias()).isEqualTo("alias");
        softly.then(selectMemberResponse.getName()).isEqualTo("이름");
        softly.then(selectMemberResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(selectMemberResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.assertAll();
    }

    @Test
    @DisplayName("회원 정보 조회 시 찾으려는 회원이 없으면 예외를 반환합니다.")
    void select_not_exist_id_throws_exception() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.selectMember(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("찾으려는 회원이 없습니다");
    }

    @Test
    @DisplayName("비밀번호를 변경한다.")
    public void changePassword() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("ChangePassword1!");
        memberService.changePassword(anyLong(), changePasswordRequest);
        verify(member, times(1)).changePassword(anyString());
    }

    @Test
    @DisplayName("회원 탈퇴에 성공한다.")
    void deleteMember() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        memberService.deleteMember(member.getId());

        verify(member, times(1)).delete();
    }

    @Test
    @DisplayName("회원 탈퇴시, 존재하지 않는 회원 식별자면 예외를 발생시킨다.")
    void deleteMember_throws_exception() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.deleteMember(member.getId()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("찾으려는 회원이 없습니다");
    }
}
