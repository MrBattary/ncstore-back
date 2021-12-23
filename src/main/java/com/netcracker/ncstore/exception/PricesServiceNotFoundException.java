package com.netcracker.ncstore.exception;

public class PricesServiceNotFoundException extends RuntimeException {
    public PricesServiceNotFoundException() {
    }

    public PricesServiceNotFoundException(String message) {
        super(message);
    }

    public PricesServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
