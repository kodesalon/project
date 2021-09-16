package com.project.kodesalon.domain.authentication;

import io.jsonwebtoken.JwtException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

import static com.project.kodesalon.exception.ErrorCode.INVALID_JWT_TOKEN;

@Slf4j
@Getter
@RedisHash(value = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken implements Serializable {

    @Id
    private Long id;

    @Indexed
    private Long memberId;

    @Indexed
    private String token;


    private LocalDateTime expiryDate;

    public RefreshToken(final Long memberId, final String token, final LocalDateTime expiryDate) {
        this.memberId = memberId;
        this.token = token;
        this.expiryDate = expiryDate;
    }

    public void replace(String token) {
        this.token = token;
    }

    public void validateExpiryDate(LocalDateTime now) {
        if (isAfter(now)) {
            log.info("{} Refresh token 만료", token);
            throw new JwtException(INVALID_JWT_TOKEN);
        }
    }

    private boolean isAfter(LocalDateTime now) {
        return now.isAfter(expiryDate);
    }
}
