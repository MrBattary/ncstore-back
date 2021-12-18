package com.netcracker.ncstore.exception;

/**
 * Used when it is impossible to create product price for provided data
 */
public class PricesServiceCreationException extends RuntimeException{
    public PricesServiceCreationException() {
    }

    public PricesServiceCreationException(String message) {
        super(message);
    }

    public PricesServiceCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
