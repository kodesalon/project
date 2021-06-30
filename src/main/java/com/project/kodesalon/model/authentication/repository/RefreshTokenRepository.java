package com.project.kodesalon.model.authentication.repository;

import com.project.kodesalon.model.authentication.domain.RefreshToken;
import com.project.kodesalon.model.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
