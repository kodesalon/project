package com.project.kodesalon.model.authentication.service;

import com.project.kodesalon.common.JwtUtils;
import com.project.kodesalon.model.authentication.domain.RefreshToken;
import com.project.kodesalon.model.authentication.repository.RefreshTokenRepository;
import com.project.kodesalon.model.authentication.service.dto.JwtResponse;
import com.project.kodesalon.model.authentication.service.dto.TokenRefreshRequest;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;
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
    private final TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest("refreshToken");

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

    @Mock
    private RefreshToken refreshToken;

    @Test
    @DisplayName("회원 별명, 비밀번호를 전달받아 Access, Refresh 토큰을 생성하고 회원 id, alias를 DTO에 담아 반환한다.")
    void login() {
        given(memberService.findMemberByAlias(anyString())).willReturn(member);
        given(jwtUtils.generateJwtToken(anyLong())).willReturn("access Token");
        LoginResponse loginResponse = authenticationTokenService.login(new LoginRequest("alias", "Password123!"));

        then(loginResponse.getAccessToken()).isNotEmpty();
        then(loginResponse.getRefreshToken()).isNotEmpty();
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

    @Test
    @DisplayName("유효한 Refresh token 값을 전달받아, Access + Refresh 토큰 모두 재발급 후 DTO에 담아 반환한다.")
    void refreshToken() {
        given(refreshTokenRepository.findByToken(anyString())).willReturn(Optional.of(refreshToken));
        JwtResponse jwtResponse = authenticationTokenService.refreshToken(tokenRefreshRequest, member);

        then(jwtResponse).isNotNull();
        verify(refreshTokenRepository, times(1)).findByToken(anyString());
    }

    @Test
    @DisplayName("인자로 받은 Refresh token이 DB에 저장되어있지 않을 경우, 예외가 발생한다.")
    void refreshToken_throw_exception_with_not_in_DB() {
        thenThrownBy(() -> authenticationTokenService.refreshToken(tokenRefreshRequest, member))
                .isInstanceOf(JwtException.class)
                .withFailMessage(INVALID_JWT_TOKEN);
    }

    @Test
    @DisplayName("인자로 받은 Refresh token이 만료된 경우, 예외가 발생한다.")
    void refreshToken_throw_exception_with_expired_token() {
        given(refreshTokenRepository.findByToken(anyString())).willReturn(Optional.of(refreshToken));
        given(refreshToken.isAfter(any(LocalDateTime.class))).willReturn(true);

        thenThrownBy(() -> authenticationTokenService.refreshToken(tokenRefreshRequest, member))
                .isInstanceOf(JwtException.class)
                .withFailMessage(INVALID_JWT_TOKEN);
    }
}
