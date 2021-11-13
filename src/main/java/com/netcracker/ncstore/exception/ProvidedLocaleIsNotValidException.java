package com.netcracker.ncstore.exception;

/**
 * This exception is used when provided locale is not valid for current task.
 */
public class ProvidedLocaleIsNotValidException extends RuntimeException{
    public ProvidedLocaleIsNotValidException() {
        super();
    }

    public ProvidedLocaleIsNotValidException(String message) {
        super(message);
    }

    public ProvidedLocaleIsNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
