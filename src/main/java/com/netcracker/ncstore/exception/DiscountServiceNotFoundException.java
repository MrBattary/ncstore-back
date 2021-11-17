package com.netcracker.ncstore.exception;

/**
 * Used when requested discount is not found
 */
public class DiscountServiceNotFoundException extends RuntimeException{
    public DiscountServiceNotFoundException() {
        super();
    }

    public DiscountServiceNotFoundException(String message) {
        super(message);
    }

    public DiscountServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
