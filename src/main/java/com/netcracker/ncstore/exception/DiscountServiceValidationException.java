package com.netcracker.ncstore.exception;

/**
 * Used when provided data for creating discount is invalid
 */
public class DiscountServiceValidationException extends RuntimeException{
    public DiscountServiceValidationException() {
        super();
    }

    public DiscountServiceValidationException(String message) {
        super(message);
    }

    public DiscountServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
