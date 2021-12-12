package com.netcracker.ncstore.exception;

/**
 * Used when it is impossible to pay the order
 */
public class OrderServiceOrderPaymentException extends RuntimeException{
    public OrderServiceOrderPaymentException() {
    }

    public OrderServiceOrderPaymentException(String message) {
        super(message);
    }

    public OrderServiceOrderPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
