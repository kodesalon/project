package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LoginResponseDto {
    private Long memberId;

    private String alias;

    public LoginResponseDto(Long id, String alias) {
        this.memberId = id;
        this.alias = alias;
    }
}
