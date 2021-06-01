package com.project.kodesalon.common;

import com.project.kodesalon.model.member.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(UnAuthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException e) {
        log.info(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        logger.info(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.info(e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.FORBIDDEN);
    }
}
