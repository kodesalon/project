package com.project.kodesalon.model.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginResponseDto {
    private Long memberId;
    private String alias;

    public LoginResponseDto(Long id, String alias) {
        this.memberId = id;
        this.alias = alias;
    }
}
