package com.netcracker.ncstore.exception;

/**
 * Used when some validation exception happens in any review service
 */
public class ReviewServiceValidationException extends RuntimeException{
    public ReviewServiceValidationException() {
    }

    public ReviewServiceValidationException(String message) {
        super(message);
    }

    public ReviewServiceValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
