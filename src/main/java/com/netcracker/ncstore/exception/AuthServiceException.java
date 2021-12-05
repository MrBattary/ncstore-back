package com.netcracker.ncstore.exception;

/**
 * Custom exception for the auth service about repository issue
 */
public class AuthServiceException extends RuntimeException {
    public AuthServiceException(String message) {
        super(message);
    }

    public AuthServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
