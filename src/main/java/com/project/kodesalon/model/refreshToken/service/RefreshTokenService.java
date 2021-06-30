package com.project.kodesalon.model.refreshToken.service;

import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.refreshToken.domain.RefreshToken;
import com.project.kodesalon.model.refreshToken.dto.RefreshTokenCreateResponse;
import com.project.kodesalon.model.refreshToken.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;

    @Value("${sping.jwt.refreshExpirationWeeks}")
    private int refreshExpirationWeeks;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
    }

    public RefreshTokenCreateResponse create(Long memberId) {
        Member member = memberService.findById(memberId);
        RefreshToken refreshToken = new RefreshToken(member, UUID.randomUUID().toString(), LocalDateTime.now().plus(refreshExpirationWeeks, ChronoUnit.WEEKS));
        refreshTokenRepository.save(refreshToken);
        return new RefreshTokenCreateResponse(refreshToken.getToken());
    }
}
