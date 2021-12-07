package com.netcracker.ncstore.exception;

/**
 * Used when provided currency is not registered in payment service
 */
public class PaymentServiceCurrencyNotSupportedException extends RuntimeException{
    public PaymentServiceCurrencyNotSupportedException() {
    }

    public PaymentServiceCurrencyNotSupportedException(String message) {
        super(message);
    }

    public PaymentServiceCurrencyNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }
}
