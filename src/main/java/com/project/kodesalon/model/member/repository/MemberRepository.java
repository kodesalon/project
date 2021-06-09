package com.project.kodesalon.model.member.repository;


import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.domain.vo.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByAlias(Alias alias);

    @Modifying
    @Query("update Member m set m.password.password = :password where m.id = :memberId")
    void updatePassword(@Param("password") String password, @Param("memberId") Long memberId);
}
