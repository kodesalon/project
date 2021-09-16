package com.project.kodesalon.repository.refreshtoken;

import com.project.kodesalon.domain.authentication.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;

@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private RefreshToken refreshToken;

    @BeforeEach
    void setUp() {
        refreshToken = new RefreshToken(1L, "token", LocalDateTime.now());
        refreshTokenRepository.save(refreshToken);
    }

    @Test
    @DisplayName("회원 식별 번호를 인자로 받아 RefreshToken을 조회한다.")
    void findByMemberId() {
        Optional<RefreshToken> selectedToken = refreshTokenRepository.findByMemberId(1L);
        then(selectedToken).isNotEmpty();
    }

    @Test
    @DisplayName("토큰을 인자로 받아 RefreshToken을 조회한다.")
    void findByToken() {
        Optional<RefreshToken> selectedToken = refreshTokenRepository.findByToken("token");
        then(selectedToken).isNotEmpty();
    }
}
