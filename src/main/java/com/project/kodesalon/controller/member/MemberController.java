package com.project.kodesalon.controller.member;

import com.project.kodesalon.config.argumentresolver.annotation.Login;
import com.project.kodesalon.service.dto.request.LoginRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private static final String BOARD_ID_MAX = "9223372036854775807";

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<Void> join(@RequestBody @Valid final MemberCreateRequest memberCreateRequest) {
        memberService.join(memberCreateRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Valid final LoginRequest loginRequest, final HttpSession session) {
        memberService.login(loginRequest, session);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(final HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<MemberSelectResponse> selectMyBoard(@Login final Long memberId, @RequestParam(required = false, defaultValue = BOARD_ID_MAX) final Long lastBoardId, @RequestParam final int size) {
        MemberSelectResponse myBoardSelectResponse = memberService.selectMember(memberId, lastBoardId, size);
        return ResponseEntity.ok().body(myBoardSelectResponse);
    }

    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(@Login final Long memberId, @RequestBody @Valid final MemberChangePasswordRequest memberChangePasswordRequest) {
        memberService.changePassword(memberId, memberChangePasswordRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMember(@Login final Long memberId, @RequestBody @Valid final MemberDeleteRequest memberDeleteRequest, final HttpSession httpSession) {
        memberService.deleteMember(memberId, memberDeleteRequest);
        httpSession.invalidate();
        return ResponseEntity.ok().build();
    }
}
