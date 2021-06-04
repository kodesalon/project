package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("존재하지 않는 Alias는 예외를 발생시킵니다.")
    void not_exist_member_login_throw_exception() {
        LoginRequestDto loginRequestDto =
                new LoginRequestDto("alias", "Password123!!");

        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .willReturn(Optional.empty());

        thenThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("존재하는 아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("존재하는 Alias가 Alias와 Password가 일치하면 Id, Alias를 리턴합니다.")
    void exist_login_return_success2() {
        LoginRequestDto loginRequestDto =
                new LoginRequestDto("alias", "Password123!!");
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
        LoginRequestDto loginRequestDto
                = new LoginRequestDto("alias", "Password123!!!");

        given(member.isIncorrectPassword(loginRequestDto.getPassword())).willReturn(true);
        given(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .willReturn(Optional.of(member));

        thenThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(HttpClientErrorException.class)
                .hasMessage("비밀 번호가 일치하지 않습니다.");
    }
}
