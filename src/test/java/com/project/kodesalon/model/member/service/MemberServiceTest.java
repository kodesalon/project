package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.SelectMemberResponseDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequestDto;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponseDto;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginRequestDto;
import com.project.kodesalon.model.member.service.dto.LoginResponseDto;
import org.assertj.core.api.BDDSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private final LoginRequestDto loginRequestDto = new LoginRequestDto("alias", "Password123!!");
    private final CreateMemberRequestDto createMemberRequestDto =
            new CreateMemberRequestDto("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222");

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("존재하지 않는 Alias는 예외를 발생시킵니다.")
    void not_exist_member_login_throw_exception() {
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .willReturn(Optional.empty());

        thenThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("존재하는 아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("존재하는 Alias가 Alias와 Password가 일치하면 Id, Alias를 리턴합니다.")
    void exist_login_return_success() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");
        given(member.hasSamePassword(loginRequestDto.getPassword())).willReturn(false);
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .willReturn(Optional.of(member));

        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        softly.then(loginResponseDto.getMemberId()).isEqualTo(1L);
        softly.then(loginResponseDto.getAlias()).isEqualTo("alias");

        softly.assertAll();
    }

    @Test
    @DisplayName("존재하는 Alias가 Password가 일치하지 않는다면 예외 메세지를 발생시킵니다.")
    void exist_login_return_fail() {
        given(member.hasSamePassword(loginRequestDto.getPassword())).willReturn(true);
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("비밀 번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 아이디이면 회원가입을 진행합니다.")
    void create_member_success() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        LoginResponseDto loginResponseDto = memberService.join(createMemberRequestDto);

        softly.then(loginResponseDto.getMemberId()).isEqualTo(1L);
        softly.then(loginResponseDto.getAlias()).isEqualTo("alias");

        softly.assertAll();
    }

    @Test
    @DisplayName("이미 존재하는 Alias면 예외를 발생시킵니다.")
    void exist_alias_throws_exception() {
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.join(createMemberRequestDto)).isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 아이디입니다");
    }

    @Test
    @DisplayName("존재하는 Alias면 회원 정보를 응답합니다.")
    void exist_id_response_member() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        given(member.getAlias()).willReturn("alias");
        given(member.getName()).willReturn("이름");
        given(member.getEmail()).willReturn("email@email.com");
        given(member.getPhone()).willReturn("010-1111-2222");
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        SelectMemberResponseDto selectMemberResponseDto = memberService.selectMember(1L);

        softly.then(selectMemberResponseDto.getAlias()).isEqualTo("alias");
        softly.then(selectMemberResponseDto.getName()).isEqualTo("이름");
        softly.then(selectMemberResponseDto.getEmail()).isEqualTo("email@email.com");
        softly.then(selectMemberResponseDto.getPhone()).isEqualTo("010-1111-2222");

        softly.assertAll();
    }

    @Test
    @DisplayName("존재하지 않는 회원을 조회하면 에외를 발생시킵니다.")
    void select_not_exist_id_throws_exception() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.selectMember(1L)).isInstanceOf(NoSuchElementException.class)
                .hasMessage("찾으려는 회원이 없습니다");
    }

    @Test
    @DisplayName("비밀번호를 변경하고 성공 메세지를 담은 DTO를 반환한다.")
    public void changePassword() {
        BDDSoftAssertions softly = new BDDSoftAssertions();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto("ChangePassword1!");
        ChangePasswordResponseDto changePasswordResponseDto = memberService.changePassword(1L, changePasswordRequestDto);
        softly.then(changePasswordResponseDto.getMessage()).isEqualTo("비밀번호 변경 성공하였습니다.");
    }
}
