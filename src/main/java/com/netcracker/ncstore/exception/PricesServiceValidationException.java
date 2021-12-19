package com.netcracker.ncstore.exception;

/**
 * Should be used when provided price data is invalid.
 * Should always have a message explaining the cause.
 */
public class PricesServiceValidationException extends RuntimeException {
    public PricesServiceValidationException(String message) {
        super(message);
    }

    public PricesServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
