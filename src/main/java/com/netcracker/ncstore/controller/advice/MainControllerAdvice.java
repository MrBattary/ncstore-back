package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.exception.ProductServiceCreationException;
import com.netcracker.ncstore.exception.RequestParametersInvalidException;
import com.netcracker.ncstore.exception.AuthServiceException;
import com.netcracker.ncstore.security.provider.JwtAuthenticationProviderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @ExceptionHandler(ProductServiceCreationException.class)
    public ResponseEntity<?> handleAuthServiceException(final ProductServiceCreationException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = { JwtAuthenticationProviderException.class })
    public void commence(HttpServletRequest request, HttpServletResponse response, JwtAuthenticationProviderException e ) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

/*    //Should be activated only on production
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAllUncaughtException(final RuntimeException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 500");
        return ResponseEntity.internalServerError().build();
    }*/
}
