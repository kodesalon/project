package com.project.kodesalon.repository.member;


import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.domain.member.vo.Alias;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByAlias(final Alias alias);

    @Query("select m from Member m join fetch m.boards where m.id = :memberId")
    Optional<Member> selectMemberById(@Param("memberId") final Long memberId);
}
