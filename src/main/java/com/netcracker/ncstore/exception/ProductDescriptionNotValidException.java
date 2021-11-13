package com.netcracker.ncstore.exception;

/**
 * Used when product description is not valid
 */
public class ProductDescriptionNotValidException extends RuntimeException {
    public ProductDescriptionNotValidException() {
    }

    public ProductDescriptionNotValidException(String message) {
        super(message);
    }

    public ProductDescriptionNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
