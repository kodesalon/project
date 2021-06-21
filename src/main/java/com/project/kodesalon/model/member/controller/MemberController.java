package com.project.kodesalon.model.member.controller;

import com.project.kodesalon.model.member.service.MemberService;
import com.project.kodesalon.model.member.service.dto.ChangePasswordRequest;
import com.project.kodesalon.model.member.service.dto.ChangePasswordResponse;
import com.project.kodesalon.model.member.service.dto.CreateMemberRequest;
import com.project.kodesalon.model.member.service.dto.LoginRequest;
import com.project.kodesalon.model.member.service.dto.LoginResponse;
import com.project.kodesalon.model.member.service.dto.SelectMemberResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin("*")
public class MemberController {
    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> logIn(@RequestBody @Valid final LoginRequest loginRequest) {
        LoginResponse loginResponse = memberService.login(loginRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PostMapping
    public ResponseEntity<LoginResponse> join(@RequestBody @Valid final CreateMemberRequest createMemberRequest) {
        LoginResponse loginResponse = memberService.join(createMemberRequest);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<SelectMemberResponse> selectMember(@PathVariable final Long memberId) {
        return new ResponseEntity<>(memberService.selectMember(memberId), HttpStatus.OK);
    }

    @PutMapping("/{memberId}")
    public ResponseEntity<ChangePasswordResponse> changePassword(@PathVariable final Long memberId, @RequestBody @Valid final ChangePasswordRequest changePasswordRequest) {
        ChangePasswordResponse changePasswordResponse = memberService.changePassword(memberId, changePasswordRequest);
        return new ResponseEntity<>(changePasswordResponse, HttpStatus.OK);
    }
}
