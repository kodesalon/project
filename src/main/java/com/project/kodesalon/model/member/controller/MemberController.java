package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.controller.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.controller.dto.LoginRequest;
import com.project.kodesalon.model.member.dto.SelectMemberResponseDto;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.LoginResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> logIn(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok()
                .body(memberService.login(loginRequest.toLoginRequestDto()));
    }

    @PostMapping
    public ResponseEntity<LoginResponseDto> join(@RequestBody CreateMemberRequest createMemberRequest) {
        return ResponseEntity.ok()
                .body(memberService.join(createMemberRequest.toCreateMemberRequestDto()));
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<SelectMemberResponseDto> selectMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.selectMember(memberId), HttpStatus.OK);
    }
}
