package com.project.kodesalon.model.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginResponseDto {

    private Long memberId;

    private String alias;

    private String message;

    public LoginResponseDto(Long id, String alias) {
        this.memberId = id;
        this.alias = alias;
    }

    public LoginResponseDto(String message) {
        this.message = message;
    }
}
