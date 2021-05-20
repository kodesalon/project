package com.project.kodesalon.model.member.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private static final String CORRECT_MEMBER_ALIAS = "alias";
    private static final String VALID_MEMBER_PASSWORD = "Password123!!";
    private static final String NOT_EXIST_MEMBER_ALIAS = "alias1234";
    private static final String NOT_CORRECT_MEMBER_PASSWORD = "Password123!!!";
    private static final Long ID = 1L;

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private Member member;

    @Test
    @DisplayName("존재하지 않는 Alias는 예외를 발생시킵니다.")
    void not_exist_member_login_throw_exception() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(NOT_EXIST_MEMBER_ALIAS, VALID_MEMBER_PASSWORD);

        when(memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias())))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.login(loginRequestDto)).isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하는 Alias를 입력해 주세요.");
    }

    @Test
    @DisplayName("존재하는 Alias가 Alias와 Password가 일치하면 200 status 코드, Id, Alias를 리턴합니다.")
    void exist_login_return_success() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(CORRECT_MEMBER_ALIAS, VALID_MEMBER_PASSWORD);

        when(member.getId()).thenReturn(ID);
        when(member.getAlias()).thenReturn(CORRECT_MEMBER_ALIAS);
        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(false);
        when(memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias())))
                .thenReturn(Optional.of(member));

        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        assertAll(
                () -> then(loginResponseDto.getHttpStatus()).isEqualTo(HttpStatus.OK),
                () -> then(loginResponseDto.getId()).isEqualTo(ID),
                () -> then(loginResponseDto.getAlias()).isEqualTo(CORRECT_MEMBER_ALIAS)
        );
    }

    @Test
    @DisplayName("존재하는 Alias가 Password가 일치하지 않는다면 401 status 코드를 리턴합니다.")
    void exist_login_return_fail() {
        LoginRequestDto loginRequestDto = new LoginRequestDto(CORRECT_MEMBER_ALIAS, NOT_CORRECT_MEMBER_PASSWORD);

        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(true);
        when(memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias())))
                .thenReturn(Optional.of(member));

        LoginResponseDto loginResponseDto = memberService.login(loginRequestDto);

        then(loginResponseDto.getHttpStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}
