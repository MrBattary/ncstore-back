package com.netcracker.ncstore.service.user;

public class UserServiceValidationException extends RuntimeException {
    public UserServiceValidationException(final String message) {
        super(message);
    }
}
