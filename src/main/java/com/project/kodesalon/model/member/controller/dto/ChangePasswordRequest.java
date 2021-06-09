package com.project.kodesalon.model.member.controller.dto;

import com.project.kodesalon.model.member.service.dto.ChangePasswordRequestDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordRequest {
    private Long memberId;
    private String password;

    public ChangePasswordRequest(Long memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }

    public ChangePasswordRequestDto toChangePasswordRequestDto() {
        return new ChangePasswordRequestDto(memberId, password);
    }
}
