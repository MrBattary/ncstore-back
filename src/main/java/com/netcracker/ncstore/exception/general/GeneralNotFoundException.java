package com.netcracker.ncstore.exception.general;

/**
 * General exception for any action ended with not found.
 * Web services should throw this exception when wrapping exceptions from business services.
 */
public class GeneralNotFoundException extends RuntimeException {
    public GeneralNotFoundException() {
    }

    public GeneralNotFoundException(String message) {
        super(message);
    }

    public GeneralNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
