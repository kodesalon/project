package com.project.kodesalon.service.authentication;

import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.service.JwtManager;
import com.project.kodesalon.service.dto.request.LoginRequest;
import com.project.kodesalon.service.dto.request.TokenRefreshRequest;
import com.project.kodesalon.service.dto.response.LoginResponse;
import com.project.kodesalon.service.dto.response.TokenResponse;
import com.project.kodesalon.service.member.MemberService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationTokenServiceTest {

    private final LoginRequest loginRequest = new LoginRequest("alias", "Password123!!");
    private final TokenRefreshRequest tokenRefreshRequest = new TokenRefreshRequest(1L, "refreshToken");

    private AuthenticationTokenService authenticationTokenService;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtManager jwtManager;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @BeforeEach
    void setUp() {
        authenticationTokenService = new AuthenticationTokenService(memberService, jwtManager,
                stringRedisTemplate, 14);
    }

    @Test
    @DisplayName("회원 별명, 비밀번호를 전달받고, refresh 토큰이 존재하지 않는다면 Access, Refresh 토큰을 생성하고 회원 id, alias를 DTO에 담아 반환한다.")
    void login() {
        Member member = mock(Member.class);
        given(memberService.findMemberByAlias(anyString())).willReturn(member);
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        given(jwtManager.generateJwtToken(anyLong())).willReturn("access Token");
        given(valueOperations.get(anyString())).willReturn(null);

        LoginResponse loginResponse = authenticationTokenService.login(new LoginRequest("alias", "Password123!"));

        then(loginResponse.getAccessToken()).isNotEmpty();
        then(loginResponse.getRefreshToken()).isNotEmpty();
        verify(memberService, times(1)).findMemberByAlias(anyString());
    }

    @Test
    @DisplayName("회원 별명, 비밀번호를 전달받고, refresh 토큰이 존재한다면 Access, Refresh 토큰을 생성하여 교체하고 회원 id, alias를 DTO에 담아 반환한다.")
    void login_with_existing_refresh_token() {
        Member member = mock(Member.class);
        given(memberService.findMemberByAlias(anyString())).willReturn(member);
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        given(jwtManager.generateJwtToken(anyLong())).willReturn("access Token");
        given(valueOperations.get(anyString())).willReturn("refresh-token");

        LoginResponse loginResponse = authenticationTokenService.login(new LoginRequest("alias", "Password123!"));

        then(loginResponse.getAccessToken()).isNotEmpty();
        then(loginResponse.getRefreshToken()).isNotEmpty();
        verify(memberService, times(1)).findMemberByAlias(anyString());
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
    void reissueAccessAndRefreshToken() {
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        given(jwtManager.generateJwtToken(anyLong())).willReturn("access-token");
        given(valueOperations.get(anyString())).willReturn("refreshToken");

        TokenResponse tokenResponse = authenticationTokenService.reissueAccessAndRefreshToken(tokenRefreshRequest);

        then(tokenResponse.getAccessToken()).isNotEmpty();
        then(tokenResponse.getRefreshToken()).isNotEmpty();
        verify(jwtManager, times(1)).generateJwtToken(anyLong());
        verify(valueOperations, times(1)).set(anyString(), anyString(), anyLong(), any(TimeUnit.class));
    }

    @Test
    @DisplayName("인자로 받은 Refresh token이 DB에 저장되어있지 않을 경우, 예외가 발생한다.")
    void reissueAccessAndRefreshToken_throw_exception_with_not_in_DB() {
        ValueOperations<String, String> valueOperations = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(anyString())).willReturn(null);

        thenThrownBy(() -> authenticationTokenService.reissueAccessAndRefreshToken(tokenRefreshRequest))
                .isInstanceOf(JwtException.class)
                .hasMessage(INVALID_JWT_TOKEN);
    }
}
