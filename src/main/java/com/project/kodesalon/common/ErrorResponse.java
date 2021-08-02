package com.project.kodesalon.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private String code;

    public ErrorResponse(String code) {
        this.code = code;
    }
}
