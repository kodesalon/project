package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
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
    private final LoginRequestDto loginRequestDto = new LoginRequestDto("alias", "Password123!!");
    private final CreateMemberRequestDto createMemberRequestDto = new CreateMemberRequestDto("alias2", "Password123!!", "이름", "email@email.com", "010-1111-2222");

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
    void exist_login_return_success2() {
        BDDSoftAssertions softly = new BDDSoftAssertions();

        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");
        given(member.isIncorrectPassword(loginRequestDto.getPassword())).willReturn(false);
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .willReturn(Optional.of(member));

        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        softly.then(loginResponseDto.getMemberId()).isEqualTo(1L);
        softly.then(loginResponseDto.getAlias()).isEqualTo("alias");

        softly.assertAll();
    }

    @Test
    @DisplayName("존재하는 Alias가 Password가 일치하지 않는다면 예외 메세지를 발생시킵니다.")
    void exist_login_return_fail2() {
        given(member.isIncorrectPassword(loginRequestDto.getPassword())).willReturn(true);
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

        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(member.getId()).willReturn(1L);
        given(member.getAlias()).willReturn("alias");

        LoginResponseDto loginResponseDto = memberService.join(createMemberRequestDto);

        softly.then(loginResponseDto.getMemberId()).isEqualTo(1L);
        softly.then(loginResponseDto.getAlias()).isEqualTo("alias");
    }

    @Test
    @DisplayName("이미 존재하는 Alias면 예외를 발생시킵니다.")
    void exist_alias_throws_exception() {
        given(memberRepository.findMemberByAlias(any(Alias.class))).willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.join(createMemberRequestDto)).isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 존재하는 아이디입니다");
    }
}
