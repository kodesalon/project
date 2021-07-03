package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.common.annotation.Login;
import com.project.kodesalon.model.member.domain.Member;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponse;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<SelectMemberResponse> selectMember(@Login Member currentMember) {
        SelectMemberResponse selectMemberResponse = memberService.selectMember(currentMember);
        return ResponseEntity.ok().body(selectMemberResponse);
    }

    @PutMapping("/password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@Login Member currentMember, @RequestBody @Valid final ChangePasswordRequest changePasswordRequest) {
        ChangePasswordResponse changePasswordResponse = memberService.changePassword(currentMember, changePasswordRequest);
        return ResponseEntity.ok().body(changePasswordResponse);
    }
}
