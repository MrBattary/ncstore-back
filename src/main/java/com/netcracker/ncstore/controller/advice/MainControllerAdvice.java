package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.exception.RequestParametersInvalidException;
import com.netcracker.ncstore.service.auth.AuthServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is primary @ControllerAdvice.
 * All custom exceptions must be handled here.
 */
@RestControllerAdvice
public class MainControllerAdvice {
    private final Logger log;

    public MainControllerAdvice() {
        log = LoggerFactory.getLogger(MainControllerAdvice.class);
    }

    @ExceptionHandler(RequestParametersInvalidException.class)
    public ResponseEntity<?> handleRequestParametersInvalidException(final RequestParametersInvalidException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<?> handleAuthServiceException(final AuthServiceException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAllUncaughtException(final RuntimeException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 500");
        return ResponseEntity.internalServerError().build();
    }
}
