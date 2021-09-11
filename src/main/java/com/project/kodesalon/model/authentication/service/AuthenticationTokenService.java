package com.project.kodesalon.model.authentication.service;

import com.project.kodesalon.common.JwtManager;
import com.project.kodesalon.model.authentication.domain.RefreshToken;
import com.project.kodesalon.model.authentication.repository.RefreshTokenRepository;
import com.project.kodesalon.model.authentication.service.dto.JwtResponse;
import com.project.kodesalon.model.authentication.service.dto.LoginRequest;
import com.project.kodesalon.model.authentication.service.dto.LoginResponse;
import com.project.kodesalon.model.authentication.service.dto.TokenRefreshRequest;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static com.project.kodesalon.common.ErrorCode.INVALID_JWT_TOKEN;

@Slf4j
@Service
public class AuthenticationTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberService memberService;
    private final JwtManager jwtManager;
    private final int refreshExpirationWeeks;

    public AuthenticationTokenService(final RefreshTokenRepository refreshTokenRepository, final MemberService memberService, final JwtManager jwtManager, @Value("${spring.jwt.refreshExpirationWeeks}") final int refreshExpirationWeeks) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberService = memberService;
        this.jwtManager = jwtManager;
        this.refreshExpirationWeeks = refreshExpirationWeeks;
    }

    @Transactional
    public LoginResponse login(final LoginRequest loginRequest) {
        String alias = loginRequest.getAlias();
        Member member = memberService.findMemberByAlias(alias);
        String password = loginRequest.getPassword();
        member.login(password);
        JwtResponse jwtResponse = issueToken(member);
        log.info("ID : {}, Alias : {} Member 로그인", member.getId(), member.getAlias());
        return new LoginResponse(jwtResponse.getAccessToken(), jwtResponse.getRefreshToken(), member.getId(), member.getAlias());
    }

    private JwtResponse issueToken(final Member member) {
        Long memberId = member.getId();
        String newRefreshToken = UUID.randomUUID().toString();
        String accessToken = jwtManager.generateJwtToken(memberId);

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
    public JwtResponse reissueAccessAndRefreshToken(final TokenRefreshRequest tokenRefreshRequest) {
        String refreshTokenFromRequest = tokenRefreshRequest.getRefreshToken();
        RefreshToken refreshToken = findByToken(refreshTokenFromRequest);
        refreshToken.validateExpiryDate(LocalDateTime.now());
        return updateToken(refreshToken.getMember().getId(), refreshToken);
    }

    private RefreshToken findByToken(final String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> {
                    log.info("{} Refresh token이 DB에 존재하지 않음", token);
                    throw new JwtException(INVALID_JWT_TOKEN);
                });
    }

    private JwtResponse updateToken(final Long memberId, final RefreshToken refreshToken) {
        String accessToken = jwtManager.generateJwtToken(memberId);
        String newRefreshToken = UUID.randomUUID().toString();
        refreshToken.replace(newRefreshToken);
        log.info("회원 ID : {}, Access Token : {}, Refresh Token : {} 토큰 재발급", memberId, accessToken, newRefreshToken);
        return new JwtResponse(accessToken, newRefreshToken);
    }
}
