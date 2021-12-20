package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(GeneralPermissionDeniedException.class)
    public ResponseEntity<String> handleGeneralPermissionDeniedException(final GeneralPermissionDeniedException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(GeneralNotFoundException.class)
    public ResponseEntity<String> handleGeneralNotFoundException(final GeneralNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(GeneralBadRequestException.class)
    public ResponseEntity<String> handleGeneralBadRequestException(final GeneralBadRequestException e) {
        log.error(e.getMessage());
        return ResponseEntity.
                badRequest().
                body(e.getMessage());
    }

/*    //Should be activated only on production
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAllUncaughtException(final RuntimeException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 500");
        return ResponseEntity.internalServerError().build();
    }*/
}
