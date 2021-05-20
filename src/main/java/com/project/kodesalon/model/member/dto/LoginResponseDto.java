package com.project.kodesalon.model.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class LoginResponseDto {
    @Getter
    private HttpStatus httpStatus;

    @Getter
    private Long id;

    @Getter
    private String alias;

    public LoginResponseDto(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public LoginResponseDto(HttpStatus httpStatus, Long id, String alias) {
        this.httpStatus = httpStatus;
        this.id = id;
        this.alias = alias;
    }
}
