package com.netcracker.ncstore.exception;

/**
 * Used when there is some exception while login
 */
public class UserServiceLoginException extends RuntimeException {
    public UserServiceLoginException() {
    }

    public UserServiceLoginException(String message) {
        super(message);
    }

    public UserServiceLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
