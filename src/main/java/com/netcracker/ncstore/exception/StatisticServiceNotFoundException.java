package com.netcracker.ncstore.exception;

/**
 * Used when the object for which statistics were requested does not exist
 */
public class StatisticServiceNotFoundException extends RuntimeException{
    public StatisticServiceNotFoundException() {
    }

    public StatisticServiceNotFoundException(String message) {
        super(message);
    }

    public StatisticServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
