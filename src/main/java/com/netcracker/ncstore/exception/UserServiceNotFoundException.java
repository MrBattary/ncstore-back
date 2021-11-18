package com.netcracker.ncstore.exception;

/**
 * Used when requested information does not exist
 */
public class UserServiceNotFoundException extends RuntimeException {
    public UserServiceNotFoundException() {
    }

    public UserServiceNotFoundException(String message) {
        super(message);
    }

    public UserServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
