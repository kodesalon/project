package com.project.kodesalon.model.member.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeleteMemberResponse {
    private String message;

    public DeleteMemberResponse(String message) {
        this.message = message;
    }
}
