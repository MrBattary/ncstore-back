package com.netcracker.ncstore.exception;

/**
 * Used when request for creating order contains invalid data
 */
public class OrderServiceValidationException extends RuntimeException{
    public OrderServiceValidationException() {
    }

    public OrderServiceValidationException(String message) {
        super(message);
    }

    public OrderServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
