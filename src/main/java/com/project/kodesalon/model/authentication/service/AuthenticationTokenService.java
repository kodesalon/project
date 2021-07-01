package com.project.kodesalon.model.authentication.service;

import com.project.kodesalon.common.JwtUtils;
import com.project.kodesalon.model.authentication.domain.RefreshToken;
import com.project.kodesalon.model.authentication.repository.RefreshTokenRepository;
import com.project.kodesalon.model.authentication.service.dto.JwtResponse;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@Slf4j
public class AuthenticationTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtUtils jwtUtils;

    @Value("${spring.jwt.refreshExpirationWeeks}")
    private int refreshExpirationWeeks;

    public AuthenticationTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService, JwtUtils jwtUtils) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String alias = loginRequest.getAlias();
        Member member = memberService.findMemberByAlias(alias);
        String password = loginRequest.getPassword();
        member.login(password);
        log.info("ID : {}, Alias : {} Member 로그인", member.getId(), member.getAlias());
        JwtResponse jwtResponse = issueToken(member.getId());
        return new LoginResponse(jwtResponse.getAccessToken(), jwtResponse.getRefreshToken(), member.getId(), member.getAlias());
    }

    private JwtResponse issueToken(Long memberId) {
        Member member = memberService.findById(memberId);
        String newRefreshToken = UUID.randomUUID().toString();
        String accessToken = jwtUtils.generateJwtToken(memberId);

        refreshTokenRepository.findById(memberId)
                .ifPresentOrElse(
                        existRefreshToken -> existRefreshToken.replace(newRefreshToken),
                        () -> {
                            RefreshToken refreshToken = new RefreshToken(member, newRefreshToken, LocalDateTime.now().plus(refreshExpirationWeeks, ChronoUnit.WEEKS));
                            refreshTokenRepository.save(refreshToken);
                        }
                );

        return new JwtResponse(accessToken, newRefreshToken);
    }
}
