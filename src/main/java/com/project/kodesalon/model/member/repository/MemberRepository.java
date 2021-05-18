package com.project.kodesalon.model.member.repository;


import com.project.kodesalon.model.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
