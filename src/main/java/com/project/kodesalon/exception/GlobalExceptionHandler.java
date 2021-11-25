package com.project.kodesalon.exception;

import com.project.kodesalon.service.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeParseException;

import static com.project.kodesalon.exception.ErrorCode.INVALID_DATE_TIME;
import static com.project.kodesalon.exception.ErrorCode.INVALID_IMAGE;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, EntityNotFoundException.class, IllegalStateException.class,
            DataIntegrityViolationException.class, SessionException.class})
    protected ResponseEntity<ErrorResponse> handleBusinessException(RuntimeException e) {
        log.info(e.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleValidationException(BindException e) {
        log.info(e.getMessage());
        String errorMessage = e.getFieldError().getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(DateTimeParseException.class)
    protected ResponseEntity<ErrorResponse> handleDateTimeParseException(DateTimeParseException e) {
        log.info(INVALID_DATE_TIME);
        return ResponseEntity.badRequest().body(new ErrorResponse(INVALID_DATE_TIME));
    }

    @ExceptionHandler(IOException.class)
    protected ResponseEntity<ErrorResponse> handleIOException(IOException e) {
        log.info(INVALID_IMAGE);
        return ResponseEntity.badRequest().body(new ErrorResponse(INVALID_IMAGE));
    }
}
