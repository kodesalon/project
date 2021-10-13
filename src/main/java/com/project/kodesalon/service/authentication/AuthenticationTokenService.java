package com.project.kodesalon.service.authentication;

import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.service.JwtManager;
import com.project.kodesalon.service.dto.request.LoginRequest;
import com.project.kodesalon.service.dto.request.TokenRefreshRequest;
import com.project.kodesalon.service.dto.response.LoginResponse;
import com.project.kodesalon.service.dto.response.TokenResponse;
import com.project.kodesalon.service.member.MemberService;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;

@Slf4j
@Service
public class AuthenticationTokenService {

    private final MemberService memberService;
    private final JwtManager jwtManager;
    private final StringRedisTemplate stringRedisTemplate;
    private final int refreshExpirationDays;

    public AuthenticationTokenService(final MemberService memberService, final JwtManager jwtManager,
                                      final StringRedisTemplate stringRedisTemplate,
                                      @Value("${spring.jwt.refreshExpirationDays}") final int refreshExpirationDays) {
        this.memberService = memberService;
        this.jwtManager = jwtManager;
        this.stringRedisTemplate = stringRedisTemplate;
        this.refreshExpirationDays = refreshExpirationDays;
    }

    @Transactional
    public LoginResponse login(final LoginRequest loginRequest) {
        String alias = loginRequest.getAlias();
        Member member = memberService.findMemberByAlias(alias);

        String password = loginRequest.getPassword();
        member.login(password);

        Long memberId = member.getId();
        TokenResponse tokenResponse = issueToken(memberId);

        String memberAlias = member.getAlias();
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        log.info("회원 ID : {}, Access Token : {}, Refresh Token : {} 로그인 토큰 발급", memberId, accessToken, refreshToken);

        return new LoginResponse(accessToken, refreshToken, memberId, memberAlias);
    }

    private TokenResponse issueToken(final Long memberId) {
        String accessToken = jwtManager.generateJwtToken(memberId);
        String refreshToken = issueRefreshToken(memberId);

        return new TokenResponse(accessToken, refreshToken);
    }

    private String issueRefreshToken(final Long memberId) {
        String newRefreshToken = UUID.randomUUID().toString();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String foundRefreshToken = valueOperations.get(memberId.toString());

        if (foundRefreshToken != null) {
            stringRedisTemplate.delete(memberId.toString());
            stringRedisTemplate.delete(foundRefreshToken);
        }

        valueOperations.set(memberId.toString(), newRefreshToken, refreshExpirationDays, TimeUnit.DAYS);
        valueOperations.set(newRefreshToken, memberId.toString(), refreshExpirationDays, TimeUnit.DAYS);

        return newRefreshToken;
    }

    @Transactional
    public TokenResponse reissueAccessAndRefreshToken(final TokenRefreshRequest tokenRefreshRequest) {
        String refreshTokenFromRequest = tokenRefreshRequest.getRefreshToken();
        Long memberId = findMemberIdByToken(refreshTokenFromRequest);
        return updateToken(memberId, refreshTokenFromRequest);
    }

    private Long findMemberIdByToken(final String token) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String memberId = valueOperations.get(token);

        if (memberId == null) {
            log.info("{} RefreshToken이 존재하지 않음", token);
            throw new JwtException(INVALID_JWT_TOKEN);
        }

        return Long.parseLong(memberId);
    }

    private TokenResponse updateToken(final Long memberId, final String refreshToken) {
        String accessToken = jwtManager.generateJwtToken(memberId);
        String newRefreshToken = reissueRefreshToken(refreshToken);
        log.info("회원 ID : {}, Access Token : {}, Refresh Token : {} 토큰 재발급", memberId, accessToken, newRefreshToken);
        return new TokenResponse(accessToken, newRefreshToken);
    }

    private String reissueRefreshToken(final String refreshToken) {
        String newRefreshToken = UUID.randomUUID().toString();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String memberId = valueOperations.get(refreshToken);

        stringRedisTemplate.delete(memberId);
        stringRedisTemplate.delete(refreshToken);

        valueOperations.set(memberId, newRefreshToken, refreshExpirationDays, TimeUnit.DAYS);
        valueOperations.set(newRefreshToken, memberId, refreshExpirationDays, TimeUnit.DAYS);

        return newRefreshToken;
    }
}
