package com.project.kodesalon.model.refreshToken.service;

import com.project.kodesalon.common.JwtUtils;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.refreshToken.domain.RefreshToken;
import com.project.kodesalon.model.refreshToken.dto.RefreshTokenCreateResponse;
import com.project.kodesalon.model.refreshToken.repository.RefreshTokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private Member member;

    @Test
    @DisplayName("회원의 식별 번호를 전달받아 Refresh 토큰을 생성하고, DTO에 토큰 값을 넣어 반환한다.")
    void create() {
        Long memberId = 1L;
        given(memberService.findById(anyLong())).willReturn(member);
        given(jwtUtils.generateRefreshToken(anyLong())).willReturn("refreshToken");
        RefreshTokenCreateResponse refreshTokenCreateResponse = refreshTokenService.create(memberId);

        then(refreshTokenCreateResponse.getRefreshToken()).isEqualTo("refreshToken");
        verify(memberService, times(1)).findById(anyLong());
        verify(jwtUtils, times(1)).generateRefreshToken(anyLong());
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }
}
