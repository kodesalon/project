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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;

@Service
@Slf4j
public class AuthenticationTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtUtils jwtUtils;
    private final int refreshExpirationWeeks;

    public AuthenticationTokenService(RefreshTokenRepository refreshTokenRepository, MemberService memberService, JwtUtils jwtUtils, @Value("${spring.jwt.refreshExpirationWeeks}") int refreshExpirationWeeks) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
        this.refreshExpirationWeeks = refreshExpirationWeeks;
    }

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        String alias = loginRequest.getAlias();
        Member member = memberService.findMemberByAlias(alias);
        String password = loginRequest.getPassword();
        member.login(password);
        JwtResponse jwtResponse = issueToken(member);
        log.info("ID : {}, Alias : {} Member 로그인", member.getId(), member.getAlias());
        return new LoginResponse(jwtResponse.getAccessToken(), jwtResponse.getRefreshToken(), member.getId(), member.getAlias());
    }

    private JwtResponse issueToken(Member member) {
        Long memberId = member.getId();
        String newRefreshToken = UUID.randomUUID().toString();
        String accessToken = jwtUtils.generateJwtToken(memberId);

        refreshTokenRepository.findByMember(member)
                .ifPresentOrElse(
                        existRefreshToken -> {
                            existRefreshToken.replace(newRefreshToken);
                            log.info("회원 ID : {}, Alias : {}, Access Token : {}, Refresh Token : {} 토큰 재발급", memberId, member.getAlias(), accessToken, newRefreshToken);
                        },
                        () -> {
                            RefreshToken refreshToken = new RefreshToken(member, newRefreshToken, LocalDateTime.now().plus(refreshExpirationWeeks, ChronoUnit.WEEKS));
                            refreshTokenRepository.save(refreshToken);
                            log.info("회원 ID : {}, Alias : {}, Access Token : {}, Refresh Token : {} 토큰 최초 발급", memberId, member.getAlias(), accessToken, newRefreshToken);
                        }
                );
        return new JwtResponse(accessToken, newRefreshToken);
    }

    @Transactional
    public JwtResponse refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        String refreshTokenFromRequest = tokenRefreshRequest.getRefreshToken();
        RefreshToken refreshToken = findByToken(refreshTokenFromRequest);
        validateExpiration(refreshToken);
        return updateToken(refreshToken.getMember().getId(), refreshToken);
    }

    private RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.info("{} Refresh token이 DB에 존재하지 않음", token);
                    throw new JwtException(INVALID_JWT_TOKEN);
                });
    }

    private void validateExpiration(RefreshToken refreshToken) {
        if (refreshToken.isAfter(LocalDateTime.now())) {
            log.info("{} Refresh token 만료", refreshToken.getToken());
            throw new JwtException(INVALID_JWT_TOKEN);
        }
    }

    private JwtResponse updateToken(Long memberId, RefreshToken refreshToken) {
        String accessToken = jwtUtils.generateJwtToken(memberId);
        String newRefreshToken = UUID.randomUUID().toString();
        refreshToken.replace(newRefreshToken);
        log.info("회원 ID : {}, Access Token : {}, Refresh Token : {} 토큰 재발급", memberId, accessToken, newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
    }
}
