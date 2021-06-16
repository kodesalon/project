package com.project.kodesalon.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatusCode());
    }

    @ExceptionHandler({IllegalArgumentException.class, NoSuchElementException.class, IllegalStateException.class})
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(RuntimeException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.info(e.getMessage());
        String errorMessage = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        return new ResponseEntity<>(new ErrorResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }
}
