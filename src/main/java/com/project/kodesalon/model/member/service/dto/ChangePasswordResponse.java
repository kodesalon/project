package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordResponse {
    private String message;

    public ChangePasswordResponse(final String message) {
        this.message = message;
    }
}
