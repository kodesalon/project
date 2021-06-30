package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponse;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{memberId}")
    public ResponseEntity<SelectMemberResponse> selectMember(@PathVariable final Long memberId) {
        SelectMemberResponse selectMemberResponse = memberService.selectMember(memberId);
        return ResponseEntity.ok().body(selectMemberResponse);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<ChangePasswordResponse> changePassword(@PathVariable final Long memberId, @RequestBody @Valid final ChangePasswordRequest changePasswordRequest) {
        ChangePasswordResponse changePasswordResponse = memberService.changePassword(memberId, changePasswordRequest);
        return ResponseEntity.ok().body(changePasswordResponse);
    }

}
