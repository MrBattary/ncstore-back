package com.netcracker.ncstore.exception;

/**
 * Used when requested role not found
 */
public class RoleServiceNotFoundException extends RuntimeException {
    public RoleServiceNotFoundException() {
    }

    public RoleServiceNotFoundException(String message) {
        super(message);
    }

    public RoleServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
