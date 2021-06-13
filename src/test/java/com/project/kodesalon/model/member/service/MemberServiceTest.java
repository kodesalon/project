package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.repository.MemberRepository;
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

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private final BDDSoftAssertions softly = new BDDSoftAssertions();
    private final LoginRequestDto loginRequestDto = new LoginRequestDto("alias", "Password123!!");
    private final CreateMemberRequestDto createMemberRequestDto = new CreateMemberRequestDto("alias", "Password123!!", "이름", "email@email.com", "010-1111-2222");

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("로그인 성공하면 회원 식별자, 별명을 담은 DTO를 반환합니다.")
    void login() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("alias", "Password123!!");
        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");
        given(member.hasSamePassword(loginRequestDto.getPassword())).willReturn(true);
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias())).willReturn(Optional.of(member));

        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        softly.then(loginResponseDto.getMemberId()).isEqualTo(1L);
        softly.then(loginResponseDto.getAlias()).isEqualTo("alias");
        softly.assertAll();
    }

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디(Alias)일 경우, 예외가 발생합니다.")
    void login_throw_exception_with_invalid_alias() {
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias())).willReturn(Optional.empty());

        thenThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessageContaining("존재하는 아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 경우, 예외 메세지를 반환합니다.")
    void login_throw_exception_with_invalid_password() {
        LoginRequestDto loginRequestDto = new LoginRequestDto("alias", "Password123!!!");

        given(member.hasSamePassword(loginRequestDto.getPassword())).willReturn(false);
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias())).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.login(loginRequestDto))
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

        LoginResponseDto loginResponseDto = memberService.join(createMemberRequestDto);

        softly.then(loginResponseDto.getMemberId()).isEqualTo(1L);
        softly.then(loginResponseDto.getAlias()).isEqualTo("alias");
        softly.assertAll();
    }

    @Test
    @DisplayName("회원가입 시 이미 존재하는 아이디(Alias)일 경우, 예외 메세지를 반환합니다.")
    void join_throw_exception_with_already_exist() {
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.join(createMemberRequestDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 아이디입니다");
    }
}
