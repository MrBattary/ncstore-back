package com.netcracker.ncstore.exception;

/**
 * Used when it is impossible to change password od user. Usually, because old!=new
 */
public class UserServiceChangePasswordException extends RuntimeException{
    public UserServiceChangePasswordException() {
    }

    public UserServiceChangePasswordException(String message) {
        super(message);
    }

    public UserServiceChangePasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
