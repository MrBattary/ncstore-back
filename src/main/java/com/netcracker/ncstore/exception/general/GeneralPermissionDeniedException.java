package com.netcracker.ncstore.exception.general;

/**
 * General exception for any action ended with not found.
 * Web services should throw this exception when wrapping exceptions from business services.
 */
public class GeneralPermissionDeniedException extends RuntimeException{
    public GeneralPermissionDeniedException() {
    }

    public GeneralPermissionDeniedException(String message) {
        super(message);
    }

    public GeneralPermissionDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
