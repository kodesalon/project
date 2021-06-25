package com.project.kodesalon.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class JwtIssueService {

    private final ObjectMapper objectMapper;

    @Value("${test.key}")
    private String secretKey = "12345678901234567890123456789000";

    public JwtIssueService(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
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

    public String issueAccessToken(LoginResponse loginResponse) throws JsonProcessingException {
        Long accessTokenValidTime = 30 * 60 * 1_000L;
        return issueToken(loginResponse, accessTokenValidTime);
    }

    public String issueRefreshToken(LoginResponse loginResponse) throws JsonProcessingException {
        Long refreshTokenValidTIme = 60 * 60 * 24 * 30 * 1_000L;
        return issueToken(loginResponse, refreshTokenValidTIme);
    }
}
