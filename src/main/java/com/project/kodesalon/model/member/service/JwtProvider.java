package com.project.kodesalon.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

import static com.project.kodesalon.common.ErrorCode.INVALID_TOKEN;

@Component
public class JwtProvider {

    private final ObjectMapper objectMapper;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public JwtProvider(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TokenDto createTokenResponse(LoginResponse loginResponse) throws JsonProcessingException {
        String accessToken = issueAccessToken(loginResponse);
        String refreshToken = issueRefreshToken(loginResponse);

        return new TokenDto(accessToken, refreshToken);
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (RuntimeException e) {
            throw new IllegalArgumentException(INVALID_TOKEN);
        }
    }

    private String issueAccessToken(LoginResponse loginResponse) throws JsonProcessingException {
        Long accessTokenValidTime = 30 * 60 * 1_000L;
        return issueToken(loginResponse, accessTokenValidTime);
    }

    private String issueRefreshToken(LoginResponse loginResponse) throws JsonProcessingException {
        Long refreshTokenValidTIme = 60 * 60 * 24 * 30 * 1_000L;
        return issueToken(loginResponse, refreshTokenValidTIme);
    }

    private String issueToken(LoginResponse loginResponse, Long validTime) throws JsonProcessingException {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(secretKeyBytes, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setHeaderParam("typ", Header.JWT_TYPE)
                .signWith(signatureAlgorithm, signingKey)
                .setSubject(objectMapper.writeValueAsString(loginResponse))
                .setExpiration(new Date(System.currentTimeMillis() + validTime))
                .compact();
    }
}
