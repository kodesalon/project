package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.domain.vo.Password;
import com.project.kodesalon.model.member.repository.MemberRepository;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequestDto;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponseDto;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponseDto;
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
    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
    private final CreateMemberRequest createMemberRequest = new CreateMemberRequest("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222");

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("로그인 성공하면 회원 식별자, 별명을 담은 DTO를 반환합니다.")
    void login() {
        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");
        given(member.hasSamePassword(new Password(loginRequest.getPassword()))).willReturn(true);
        given(memberRepository.findMemberByAlias(new Alias(loginRequest.getAlias()))).willReturn(Optional.of(member));

        LoginResponse loginResponse = memberService.login(loginRequest);

        softly.then(loginResponse.getMemberId()).isEqualTo(1L);
        softly.then(loginResponse.getAlias()).isEqualTo("alias");
        softly.assertAll();
    }

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디(Alias)일 경우, 예외가 발생합니다.")
    void login_throw_exception_with_invalid_alias() {
        given(memberRepository.findMemberByAlias(new Alias(loginRequest.getAlias()))).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("존재하는 아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 경우, 예외 메세지를 반환합니다.")
    void login_throw_exception_with_invalid_password() {
        given(member.hasSamePassword(new Password(loginRequest.getPassword()))).willReturn(false);
        given(memberRepository.findMemberByAlias(new Alias(loginRequest.getAlias()))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.login(loginRequest))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("비밀 번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("회원가입이 성공하면 회원가입한 회원 식별자, 별명을 담은 DTO를 반환합니다.")
    void join() {
        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);

        LoginResponse loginResponse = memberService.join(createMemberRequest);

        softly.then(loginResponse.getMemberId()).isEqualTo(1L);
        softly.then(loginResponse.getAlias()).isEqualTo("alias");
        softly.assertAll();
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 아이디(Alias)일 경우, 예외 메세지를 반환합니다.")
    void join_throw_exception_with_already_exist() {
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.join(createMemberRequest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 아이디입니다");
    }

    @Test
    @DisplayName("회원정보 조회 성공 시, 회원 별명, 이름, 이메일, 전화 번호를 반환합니다.")
    void exist_id_response_member() {
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
    @DisplayName("회원 정보 조회 시 찾으려는 회원이 없으면 예외를 반환합니다.")
    void select_not_exist_id_throws_exception() {
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.selectMember(1L))
                .isInstanceOf(NoSuchElementException.class)
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
