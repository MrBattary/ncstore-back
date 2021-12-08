package com.netcracker.ncstore.exception;

public class OrderServicePermissionException extends RuntimeException {
    public OrderServicePermissionException() {
    }

    public OrderServicePermissionException(String message) {
        super(message);
    }

    public OrderServicePermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
