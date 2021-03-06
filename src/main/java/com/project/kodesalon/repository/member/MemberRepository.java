package com.project.kodesalon.repository.member;


import com.project.kodesalon.domain.member.Member;
import com.project.kodesalon.domain.member.vo.Alias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findMemberByAlias(final Alias alias);
}
