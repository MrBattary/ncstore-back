package com.netcracker.ncstore.exception;

/**
 * Used when some payment error occurs
 */
public class PaymentServiceException extends RuntimeException{
    public PaymentServiceException() {
    }

    public PaymentServiceException(String message) {
        super(message);
    }

    public PaymentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
