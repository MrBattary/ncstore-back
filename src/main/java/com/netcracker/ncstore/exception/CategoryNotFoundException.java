package com.netcracker.ncstore.exception;

/**
 * This exception is used when category could not be found
 * by using provided parameters
 */
public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException() {
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }

    public CategoryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
