package com.project.kodesalon.repository.refreshtoken;

import com.project.kodesalon.domain.authentication.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByMemberId(final Long memberId);

    Optional<RefreshToken> findByToken(String token);
}
