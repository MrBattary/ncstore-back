package com.netcracker.ncstore.exception;

/**
 * Used in CartService when passed parameters are invalid
 */
public class CartServiceValidationException extends RuntimeException{
    public CartServiceValidationException() {
    }

    public CartServiceValidationException(String message) {
        super(message);
    }

    public CartServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
