package com.netcracker.ncstore.service.user;

public class UserServiceCreationException extends RuntimeException {
    public UserServiceCreationException(String message) {
        super(message);
    }

    public UserServiceCreationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
