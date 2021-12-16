package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.exception.AuthServiceException;
import com.netcracker.ncstore.exception.CartServiceCheckoutException;
import com.netcracker.ncstore.exception.CartServiceValidationException;
import com.netcracker.ncstore.exception.GeneralBadRequestException;
import com.netcracker.ncstore.exception.GeneralNotFoundException;
import com.netcracker.ncstore.exception.GeneralPermissionDeniedException;
import com.netcracker.ncstore.exception.PaymentServiceCurrencyNotSupportedException;
import com.netcracker.ncstore.exception.PaymentServiceException;
import com.netcracker.ncstore.exception.ProductServiceCreationException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundExpectedException;
import com.netcracker.ncstore.exception.RequestParametersInvalidException;
import com.netcracker.ncstore.exception.UserServiceChangePasswordException;
import com.netcracker.ncstore.exception.UserServiceCompanyInfoException;
import com.netcracker.ncstore.exception.UserServiceNotFoundException;
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
    public ResponseEntity<?> handleGeneralPermissionDeniedException(final GeneralPermissionDeniedException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 403");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(GeneralNotFoundException.class)
    public ResponseEntity<?> handleGeneralNotFoundException(final GeneralNotFoundException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(GeneralBadRequestException.class)
    public ResponseEntity<?> handleGeneralBadRequestException(final GeneralBadRequestException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RequestParametersInvalidException.class)
    public ResponseEntity<?> handleRequestParametersInvalidException(final RequestParametersInvalidException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<?> handleAuthServiceException(final AuthServiceException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ProductServiceCreationException.class)
    public ResponseEntity<?> handleAuthServiceException(final ProductServiceCreationException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserServiceCompanyInfoException.class)
    public ResponseEntity<?> handleAuthServiceException(final UserServiceCompanyInfoException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserServiceNotFoundException.class)
    public ResponseEntity<?> handleAuthServiceException(final UserServiceNotFoundException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 404");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CartServiceValidationException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final CartServiceValidationException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ProductServiceNotFoundExpectedException.class)
    public ResponseEntity<?> handleProductServiceNotFoundExpectedException(final ProductServiceNotFoundExpectedException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 204");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProductServiceNotFoundException.class)
    public ResponseEntity<?> handleProductServiceNotFoundException(final ProductServiceNotFoundException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 404");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(CartServiceCheckoutException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final CartServiceCheckoutException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(UserServiceChangePasswordException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final UserServiceChangePasswordException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PaymentServiceException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final PaymentServiceException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(PaymentServiceCurrencyNotSupportedException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final PaymentServiceCurrencyNotSupportedException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    //Should be activated only on production
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAllUncaughtException(final RuntimeException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 500");
        return ResponseEntity.internalServerError().build();
    }
}
