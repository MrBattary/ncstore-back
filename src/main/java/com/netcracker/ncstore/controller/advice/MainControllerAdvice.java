package com.netcracker.ncstore.controller.advice;

import com.netcracker.ncstore.exception.*;
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
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(GeneralNotFoundException.class)
    public ResponseEntity<String> handleGeneralNotFoundException(final GeneralNotFoundException e) {
        log.error(e.getMessage());
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(GeneralBadRequestException.class)
    public ResponseEntity<String> handleGeneralBadRequestException(final GeneralBadRequestException e) {
        log.error(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
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

    @ExceptionHandler(UserServiceCompanyInfoException.class)
    public ResponseEntity<?> handleAuthServiceException(final UserServiceCompanyInfoException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UserServiceNotFoundException.class)
    public ResponseEntity<?> handleAuthServiceException(final UserServiceNotFoundException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 404");
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CartServiceValidationException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final CartServiceValidationException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
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
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CartServiceCheckoutException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final CartServiceCheckoutException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UserServiceChangePasswordException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final UserServiceChangePasswordException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PaymentServiceException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final PaymentServiceException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(PaymentServiceCurrencyNotSupportedException.class)
    public ResponseEntity<?> handleCartServiceValidationException(final PaymentServiceCurrencyNotSupportedException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 400");
        return ResponseEntity.badRequest().build();
    }

/*    //Should be activated only on production
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleAllUncaughtException(final RuntimeException e) {
        log.error(e.getMessage());
        log.info("RESPONSE: 500");
        return ResponseEntity.internalServerError().build();
    }*/
}
