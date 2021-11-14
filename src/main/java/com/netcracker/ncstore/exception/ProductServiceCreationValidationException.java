package com.netcracker.ncstore.exception;

/**
 * Should be used when parameters for new product are invalid.
 * Should always have a message explaining the cause;
 */
public class ProductServiceCreationValidationException extends RuntimeException{
    public ProductServiceCreationValidationException(String message) {
        super(message);
    }

    public ProductServiceCreationValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
