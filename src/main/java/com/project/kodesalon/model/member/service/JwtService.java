package com.project.kodesalon.model.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;

@Service
public class JwtService {

    private final ObjectMapper objectMapper;
    private final JwtIssueService jwtIssueService;

    @Value("${test.key}")
    private String secretKey = "12345678901234567890123456789000";

    public JwtService(final ObjectMapper objectMapper, final JwtIssueService jwtIssueService) {
        this.objectMapper = objectMapper;
        this.jwtIssueService = jwtIssueService;
    }

    public Long getPayload(String token) throws JsonProcessingException {
        Claims claims = getAllClaimsFromToken(token);
        return objectMapper.readValue(claims.getSubject(), Long.class);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                .parseClaimsJws(token)
                .getBody();
    }

    public TokenDto createTokenResponse(LoginResponse loginResponse) throws JsonProcessingException {
        String accessToken = jwtIssueService.issueAccessToken(loginResponse);
        String refreshToken = jwtIssueService.issueRefreshToken(loginResponse);

        return new TokenDto(accessToken, refreshToken);
    }
}
