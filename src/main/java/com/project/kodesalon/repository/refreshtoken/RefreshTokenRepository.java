package com.project.kodesalon.repository.refreshtoken;

import com.project.kodesalon.domain.authentication.RefreshToken;
import com.project.kodesalon.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMember(Member member);

    Optional<RefreshToken> findByToken(String token);
}
