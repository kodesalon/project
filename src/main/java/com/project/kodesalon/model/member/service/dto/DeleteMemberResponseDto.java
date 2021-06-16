package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class DeleteMemberResponseDto {
    private String message;

    public DeleteMemberResponseDto(String message) {
        this.message = message;
    }
}
