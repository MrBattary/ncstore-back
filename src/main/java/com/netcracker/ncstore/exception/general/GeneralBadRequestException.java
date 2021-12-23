package com.netcracker.ncstore.exception.general;

public class GeneralBadRequestException extends RuntimeException {
    public GeneralBadRequestException() {
    }

    public GeneralBadRequestException(String message) {
        super(message);
    }

    public GeneralBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
