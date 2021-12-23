package com.netcracker.ncstore.exception;

/**
 * Used when there is some error while changing user's password
 */
public class UserServicePasswordChangingException extends RuntimeException {
    public UserServicePasswordChangingException() {
    }

    public UserServicePasswordChangingException(String message) {
        super(message);
    }

    public UserServicePasswordChangingException(String message, Throwable cause) {
        super(message, cause);
    }
}
