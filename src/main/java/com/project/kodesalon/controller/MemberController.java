package com.project.kodesalon.controller;

import com.project.kodesalon.common.annotation.Login;
import com.project.kodesalon.service.MemberService;
import com.project.kodesalon.service.dto.request.ChangePasswordRequest;
import com.project.kodesalon.service.dto.request.CreateMemberRequest;
import com.project.kodesalon.service.dto.request.DeleteMemberRequest;
import com.project.kodesalon.service.dto.response.SelectMemberResponse;
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
    public ResponseEntity<Void> join(@RequestBody @Valid final CreateMemberRequest createMemberRequest) {
        memberService.join(createMemberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<SelectMemberResponse> selectMember(@Login final Long memberId) {
        SelectMemberResponse selectMemberResponse = memberService.selectMember(memberId);
        return ResponseEntity.ok().body(selectMemberResponse);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Login final Long memberId, @RequestBody @Valid final ChangePasswordRequest changePasswordRequest) {
        memberService.changePassword(memberId, changePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Login final Long memberId, @RequestBody @Valid final DeleteMemberRequest deleteMemberRequest) {
        memberService.deleteMember(memberId, deleteMemberRequest);
        return ResponseEntity.ok().build();
    }
}
