package com.project.kodesalon.model.refreshToken.service;

import com.project.kodesalon.common.JwtUtils;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.refreshToken.domain.RefreshToken;
import com.project.kodesalon.model.refreshToken.dto.RefreshTokenCreateResponse;
import com.project.kodesalon.model.refreshToken.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    public RefreshTokenCreateResponse create(Long memberId) {
        Member member = memberService.findById(memberId);
        String token = jwtUtils.generateRefreshToken(memberId);
        RefreshToken refreshToken = new RefreshToken(member, token, new Date());
        refreshTokenRepository.save(refreshToken);
        return new RefreshTokenCreateResponse(refreshToken.getToken());
    }
}
