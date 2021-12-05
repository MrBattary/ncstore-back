package com.netcracker.ncstore.exception;

/**
 * Used when cart can not checkout
 */
public class CartServiceCheckoutException extends RuntimeException{
    public CartServiceCheckoutException() {
    }

    public CartServiceCheckoutException(String message) {
        super(message);
    }

    public CartServiceCheckoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
