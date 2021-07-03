package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponse;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("회원정보 조회 성공 시, 회원 별명, 이름, 이메일, 전화 번호를 반환합니다.")
    void exist_id_response_member() {
        given(member.getAlias()).willReturn("alias");
        given(member.getName()).willReturn("이름");
        given(member.getEmail()).willReturn("email@email.com");
        given(member.getPhone()).willReturn("010-1111-2222");

        SelectMemberResponse selectMemberResponse = memberService.selectMember(member);

        softly.then(selectMemberResponse.getAlias()).isEqualTo("alias");
        softly.then(selectMemberResponse.getName()).isEqualTo("이름");
        softly.then(selectMemberResponse.getEmail()).isEqualTo("email@email.com");
        softly.then(selectMemberResponse.getPhone()).isEqualTo("010-1111-2222");
        softly.assertAll();
    }

    @Test
    @DisplayName("비밀번호를 변경하고 성공 메세지를 담은 DTO를 반환한다.")
    public void changePassword() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest("ChangePassword1!");
        ChangePasswordResponse changePasswordResponse = memberService.changePassword(member, changePasswordRequest);
        then(changePasswordResponse.getMessage()).isEqualTo("비밀번호 변경 성공하였습니다.");
    }
}
