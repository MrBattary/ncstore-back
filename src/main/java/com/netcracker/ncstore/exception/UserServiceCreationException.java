package com.netcracker.ncstore.exception;

/**
 * Exception when it is unable to create a user
 */
public class UserServiceCreationException extends RuntimeException {
    public UserServiceCreationException(String message) {
        super(message);
    }

    public UserServiceCreationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
