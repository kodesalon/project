package com.project.kodesalon.model.authentication.service;

import com.project.kodesalon.common.JwtUtils;
import com.project.kodesalon.model.authentication.domain.RefreshToken;
import com.project.kodesalon.model.authentication.repository.RefreshTokenRepository;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationTokenServiceTest {
    private final LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");

    @InjectMocks
    private AuthenticationTokenService authenticationTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Member member;

    @Test
    @DisplayName("회원 별명, 비밀번호를 전달받아 Access, Refresh 토큰을 생성하고 회원 id, alias를 DTO에 담아 반환한다.")
    void login() {
        given(memberService.findById(anyLong())).willReturn(member);
        given(memberService.findMemberByAlias(anyString())).willReturn(member);
        given(jwtUtils.generateJwtToken(anyLong())).willReturn("access Token");
        LoginResponse loginResponse = authenticationTokenService.login(new LoginRequest("alias", "Password123!"));

        then(loginResponse.getAccessToken()).isNotEmpty();
        then(loginResponse.getRefreshToken()).isNotEmpty();
        verify(memberService, times(1)).findById(anyLong());
        verify(memberService, times(1)).findMemberByAlias(anyString());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    @DisplayName("로그인 시 존재하지 않는 아이디(Alias)일 경우, 예외가 발생합니다.")
    void login_throw_exception_with_invalid_alias() {
        given(memberService.findMemberByAlias(anyString())).willThrow(new NoSuchElementException("존재하는 아이디를 입력해주세요."));

        thenThrownBy(() -> authenticationTokenService.login(loginRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하는 아이디를 입력해주세요.");
    }

    @Test
    @DisplayName("로그인 시 비밀번호 틀렸을 경우, 예외 메세지를 반환합니다.")
    void login_throw_exception_with_invalid_password() {
        given(memberService.findMemberByAlias(anyString())).willThrow(new IllegalArgumentException("비밀 번호가 일치하지 않습니다."));

        thenThrownBy(() -> authenticationTokenService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀 번호가 일치하지 않습니다.");
    }
}
