package com.netcracker.ncstore.service.user;

/**
 * Exception when some type of error with repository occurs
 */
public class UserServiceRepositoryException extends RuntimeException {
    public UserServiceRepositoryException(final String message) {
        super(message);
    }
}
