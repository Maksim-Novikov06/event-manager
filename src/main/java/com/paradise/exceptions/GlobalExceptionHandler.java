package com.paradise.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgument(Exception e) {
        log.error(e.getMessage());

        ErrorMessageResponse emr = new ErrorMessageResponse(
                "INTERNAL SERVER ERROR",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emr);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgument(EntityNotFoundException e) {
        log.error(e.getMessage());

        ErrorMessageResponse emr = new ErrorMessageResponse(
                "NOT_FOUND",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(emr);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessageResponse> handleIllegalArgument(EntityExistsException e) {
        log.error(e.getMessage());

        ErrorMessageResponse emr = new ErrorMessageResponse(
                "BAD_REQUEST",
                e.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emr);
    }

}
