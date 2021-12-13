package com.netcracker.ncstore.exception;

public class OrderServiceNotFoundException extends RuntimeException{
    public OrderServiceNotFoundException() {
    }

    public OrderServiceNotFoundException(String message) {
        super(message);
    }

    public OrderServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
