package com.netcracker.ncstore.service.user;

public class UserServiceBuildingException extends RuntimeException {
    public UserServiceBuildingException(String message) {
        super(message);
    }

    public UserServiceBuildingException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
