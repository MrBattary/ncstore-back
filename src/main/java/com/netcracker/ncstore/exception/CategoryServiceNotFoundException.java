package com.netcracker.ncstore.exception;

/**
 * This exception is used when category could not be found
 * by using provided parameters
 */
public class CategoryServiceNotFoundException extends RuntimeException {
    public CategoryServiceNotFoundException() {
    }

    public CategoryServiceNotFoundException(String message) {
        super(message);
    }

    public CategoryServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
