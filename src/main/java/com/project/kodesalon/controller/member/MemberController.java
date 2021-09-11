package com.project.kodesalon.controller.member;

import com.project.kodesalon.config.argumentresolver.annotation.Login;
import com.project.kodesalon.service.dto.request.MemberChangePasswordRequest;
import com.project.kodesalon.service.dto.request.MemberCreateRequest;
import com.project.kodesalon.service.dto.request.MemberDeleteRequest;
import com.project.kodesalon.service.dto.response.MemberSelectResponse;
import com.project.kodesalon.service.member.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody @Valid final MemberCreateRequest memberCreateRequest) {
        memberService.join(memberCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberSelectResponse> selectMember(@Login final Long memberId) {
        MemberSelectResponse memberSelectResponse = memberService.selectMember(memberId);
        return ResponseEntity.ok().body(memberSelectResponse);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Login final Long memberId, @RequestBody @Valid final MemberChangePasswordRequest memberChangePasswordRequest) {
        memberService.changePassword(memberId, memberChangePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Login final Long memberId, @RequestBody @Valid final MemberDeleteRequest memberDeleteRequest) {
        memberService.deleteMember(memberId, memberDeleteRequest);
        return ResponseEntity.ok().build();
    }
}
