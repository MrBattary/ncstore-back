package com.netcracker.ncstore.exception;

/**
 * this exception is used when it is impossible to create product due to some problem
 */
public class ProductCreationException extends RuntimeException{
    public ProductCreationException() {
    }

    public ProductCreationException(String message) {
        super(message);
    }

    public ProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
