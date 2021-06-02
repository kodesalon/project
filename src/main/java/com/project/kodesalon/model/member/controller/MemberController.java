package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.dto.CreateMemberRequestDto;
import com.project.kodesalon.model.member.dto.LoginRequestDto;
import com.project.kodesalon.model.member.dto.LoginResponseDto;
import com.project.kodesalon.model.member.dto.SelectMemberResponseDto;
import com.project.kodesalon.model.member.service.MemberService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<LoginResponseDto> logIn(@RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(memberService.login(loginRequestDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LoginResponseDto> join(@RequestBody CreateMemberRequestDto createMemberRequestDto) {
        return new ResponseEntity<>(memberService.join(createMemberRequestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<SelectMemberResponseDto> selectMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.selectMember(memberId), HttpStatus.OK);
    }
}