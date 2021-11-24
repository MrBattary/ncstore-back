package com.netcracker.ncstore.exception;

/**
 * Used when there is an exception while creating order
 */
public class OrderServiceOrderCreationException extends RuntimeException{
    public OrderServiceOrderCreationException() {
    }

    public OrderServiceOrderCreationException(String message) {
        super(message);
    }

    public OrderServiceOrderCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
