package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

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

        when(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("존재하는 아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("존재하는 Alias가 Alias와 Password가 일치하면 200 status 코드, Id, Alias를 리턴합니다.")
    void exist_login_return_success2() {
        LoginRequestDto loginRequestDto =
                new LoginRequestDto("alias", "Password123!!");

        when(member.getId()).thenReturn(1L);
        when(member.getAlias()).thenReturn("alias");
        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(false);
        when(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .thenReturn(Optional.of(member));

        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        assertAll(
                () -> then(loginResponseDto.getMemberId()).isEqualTo(1L),
                () -> then(loginResponseDto.getAlias()).isEqualTo("alias")
        );
    }

    @Test
    @DisplayName("존재하는 Alias가 Password가 일치하지 않는다면 예외 메세지를 발생시킵니다.")
    void exist_login_return_fail2() {
        LoginRequestDto loginRequestDto
                = new LoginRequestDto("alias", "Password123!!!");

        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(true);
        when(memberRepository.findMemberByAlias(loginRequestDto.getAlias()))
                .thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("비밀 번호가 일치하지 않습니다.");
    }
}
