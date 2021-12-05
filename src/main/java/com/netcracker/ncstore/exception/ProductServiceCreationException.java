package com.netcracker.ncstore.exception;

/**
 * this exception is used when it is impossible to create product due to some problem
 */
public class ProductServiceCreationException extends RuntimeException {
    public ProductServiceCreationException() {
    }

    public ProductServiceCreationException(String message) {
        super(message);
    }

    public ProductServiceCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
