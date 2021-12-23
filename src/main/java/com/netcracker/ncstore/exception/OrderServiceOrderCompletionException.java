package com.netcracker.ncstore.exception;

/**
 * Used when there is exception while completing order
 */
public class OrderServiceOrderCompletionException extends RuntimeException {
    public OrderServiceOrderCompletionException() {
    }

    public OrderServiceOrderCompletionException(String message) {
        super(message);
    }

    public OrderServiceOrderCompletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
