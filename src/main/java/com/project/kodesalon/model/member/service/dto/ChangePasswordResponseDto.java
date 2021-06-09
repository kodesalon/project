package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordResponseDto {
    private String message;

    public ChangePasswordResponseDto(String message) {
        this.message = message;
    }
}
