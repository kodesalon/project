package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.controller.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.controller.dto.LoginRequest;
import com.project.kodesalon.model.member.dto.SelectMemberResponseDto;
import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logIn(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping
    public ResponseEntity<LoginResponse> join(@RequestBody @Valid CreateMemberRequest createMemberRequest) {
        LoginResponse loginResponse = memberService.join(createMemberRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<SelectMemberResponseDto> selectMember(@PathVariable Long memberId) {
        return new ResponseEntity<>(memberService.selectMember(memberId), HttpStatus.OK);
    }
}
