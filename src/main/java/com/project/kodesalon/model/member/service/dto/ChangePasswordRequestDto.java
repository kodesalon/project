package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordRequestDto {
    private Long memberId;
    private String password;

    public ChangePasswordRequestDto(Long memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }
}
