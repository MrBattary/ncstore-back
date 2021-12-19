package com.netcracker.ncstore.exception;

/**
 * Used when there was and error while registering new user
 */
public class UserServiceRegistrationException extends RuntimeException{
    public UserServiceRegistrationException() {
    }

    public UserServiceRegistrationException(String message) {
        super(message);
    }

    public UserServiceRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
