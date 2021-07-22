package com.project.kodesalon.repository;


import com.project.kodesalon.domain.Member;
import com.project.kodesalon.domain.vo.Alias;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByAlias(Alias alias);
}
