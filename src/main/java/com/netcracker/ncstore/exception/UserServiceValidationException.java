package com.netcracker.ncstore.exception;

/**
 * Exception if provided data is invalid
 */
public class UserServiceValidationException extends RuntimeException {
    public UserServiceValidationException(final String message) {
        super(message);
    }
}
