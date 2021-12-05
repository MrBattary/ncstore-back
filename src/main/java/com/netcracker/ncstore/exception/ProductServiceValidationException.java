package com.netcracker.ncstore.exception;

/**
 * Should be used when parameters for new product are invalid.
 * Should always have a message explaining the cause;
 */
public class ProductServiceValidationException extends RuntimeException{
    public ProductServiceValidationException(String message) {
        super(message);
    }

    public ProductServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
