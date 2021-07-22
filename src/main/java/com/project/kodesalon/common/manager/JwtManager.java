package com.project.kodesalon.common.manager;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

import static com.project.kodesalon.common.code.ErrorCode.EXPIRED_JWT_TOKEN;
import static com.project.kodesalon.common.code.ErrorCode.INVALID_JWT_TOKEN;

@Slf4j
@Component
public class JwtManager {

    private static final String MEMBER_ID = "memberId";

    private final String secretKey;
    private final long accessExpirationMs;

    public JwtManager(@Value("${spring.jwt.secret}") final String secretKey, @Value("${spring.jwt.accessExpirationMs}") final long accessExpirationMs) {
        this.secretKey = secretKey;
        this.accessExpirationMs = accessExpirationMs;
    }

    public String generateJwtToken(final Long memberId) {
        Date issueTime = new Date();
        return Jwts.builder()
                .setHeaderParam("typ", Header.JWT_TYPE)
                .claim(MEMBER_ID, memberId)
                .setIssuedAt(issueTime)
                .setExpiration(new Date(issueTime.getTime() + accessExpirationMs))
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    public Long getMemberIdFrom(final String token) {
        return Jwts.parser()
                .setSigningKey(getSignKey())
                .parseClaimsJws(token)
                .getBody()
                .get(MEMBER_ID, Long.class);
    }

    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .setSigningKey(getSignKey())
                    .parseClaimsJws(token);

            return true;
        } catch (SignatureException e) {
            log.info("Invalid JWT signature: {}", e.getMessage());
            throw new JwtException(INVALID_JWT_TOKEN);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token: {}", e.getMessage());
            throw new JwtException(INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("JWT token is expired: {}", e.getMessage());
            throw new JwtException(EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("JWT token is unsupported: {}", e.getMessage());
            throw new JwtException(INVALID_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty: {}", e.getMessage());
            throw new JwtException(INVALID_JWT_TOKEN);
        }
    }

    private Key getSignKey() {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = secretKey.getBytes();
        return new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());
    }
}

