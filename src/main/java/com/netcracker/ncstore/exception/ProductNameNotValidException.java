package com.netcracker.ncstore.exception;

/**
 * Used when product name is not valid
 */
public class ProductNameNotValidException extends RuntimeException{
    public ProductNameNotValidException() {
    }

    public ProductNameNotValidException(String message) {
        super(message);
    }

    public ProductNameNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
