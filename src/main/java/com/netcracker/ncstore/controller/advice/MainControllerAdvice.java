package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.exception.RequestParametersInvalidException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * This class is primary @ControllerAdvice.
 * All custom exceptions must be handled here.
 */
@RestControllerAdvice
public class MainControllerAdvice {

    @ExceptionHandler(value = {RequestParametersInvalidException.class})
    public ResponseEntity<?> handleRequestParametersInvalidException(
            RequestParametersInvalidException exception) {

        return ResponseEntity.
                badRequest().
                contentType(MediaType.APPLICATION_JSON).
                body(exception.getMessage());
    }
}
