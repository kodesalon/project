package com.project.kodesalon.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatusCode());
    }
}
