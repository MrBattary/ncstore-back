package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.service.auth.AuthServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is controller advice for the AuthController
 */
@RestControllerAdvice
public class AuthControllerAdvice {
    private final Logger log;

    public AuthControllerAdvice() {
        log = LoggerFactory.getLogger(MainControllerAdvice.class);
    }

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<?> handleAuthServiceGeneralException(final AuthServiceException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }
}
