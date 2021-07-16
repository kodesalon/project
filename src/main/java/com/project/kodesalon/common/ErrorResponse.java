package com.project.kodesalon.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ErrorResponse {
    private String code;

    public ErrorResponse(String code) {
        this.code = code;
    }
}
