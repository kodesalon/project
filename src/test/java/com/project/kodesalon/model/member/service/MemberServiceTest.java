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
import org.springframework.http.ResponseEntity;

import java.util.Objects;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private static final String NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE = "존재하는 Alias를 입력해주세요.";
    private static final String PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE = "일치하는 비밀번호를 입력해주세요.";
    private static final String CORRECT_MEMBER_ALIAS = "alias";
    private static final String VALID_MEMBER_PASSWORD = "Password123!!";
    private static final String NOT_EXIST_MEMBER_ALIAS = "alias1234";
    private static final String NOT_CORRECT_MEMBER_PASSWORD = "Password123!!!";
    private static final Long MEMBER_ID = 1L;
    private static final String NAME = "이름";
    private static final String EMAIL = "email@email.com";
    private static final String PHONE = "010-1111-2222";

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
                new LoginRequestDto(NOT_EXIST_MEMBER_ALIAS, VALID_MEMBER_PASSWORD);

        when(memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias())))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(NO_MEMBER_ELEMENT_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("존재하는 Alias가 Alias와 Password가 일치하면 200 status 코드, Id, Alias를 리턴합니다.")
    void exist_login_return_success2() {
        LoginRequestDto loginRequestDto =
                new LoginRequestDto(CORRECT_MEMBER_ALIAS, VALID_MEMBER_PASSWORD);

        when(member.getId()).thenReturn(MEMBER_ID);
        when(member.getAlias()).thenReturn(CORRECT_MEMBER_ALIAS);
        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(false);
        when(memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias())))
                .thenReturn(Optional.of(member));

        ResponseEntity<LoginResponseDto> loginResponseDto = memberService.login(loginRequestDto);

        assertAll(
                () -> then(Objects.requireNonNull(loginResponseDto.getBody())
                        .getMemberId())
                        .isEqualTo(MEMBER_ID),
                () -> then(Objects.requireNonNull(loginResponseDto.getBody())
                        .getAlias())
                        .isEqualTo(CORRECT_MEMBER_ALIAS)
        );
    }

    @Test
    @DisplayName("존재하는 Alias가 Password가 일치하지 않는다면 예외 메세지를 발생시킵니다.")
    void exist_login_return_fail2() {
        LoginRequestDto loginRequestDto
                = new LoginRequestDto(CORRECT_MEMBER_ALIAS, NOT_CORRECT_MEMBER_PASSWORD);

        when(member.isIncorrectPassword(loginRequestDto.getPassword())).thenReturn(true);
        when(memberRepository.findMemberByAlias(new Alias(loginRequestDto.getAlias())))
                .thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.login(loginRequestDto))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage(PASSWORD_NOT_MATCH_EXCEPTION_MESSAGE);
    }

    @Test
    @DisplayName("존재하지 않는 Alias면 회원가입을 진행하고 Login Response를 반환합니다")
    void not_existing_member_save_member() {
        CreateMemberRequestDto createMemberRequestDto
                = new CreateRequestDto(CORRECT_MEMBER_ALIAS,
                VALID_MEMBER_PASSWORD, NAME, EMAIL, PHONE);

        when(memberRepository.findMemberByAlias(new Alias(createMemberRequestDto.getAlias())))
                .thenReturn(Optional.empty());
        LoginResponseDto loginResponseDto = memberService.joinMember(createMemberRequestDto);

        assertAll(
                () -> then(loginResponseDto.getMemberId()).isEqualTo(MEMBER_ID),
                () -> then(loginResponseDto.getAlias()).isEqualTo(CORRECT_MEMBER_ALIAS)
        );
    }
}
