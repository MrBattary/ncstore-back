package com.netcracker.ncstore.security.filter;

/**
 * Custom filter exception
 */
public class JwtAuthFilterException extends RuntimeException {
    /**
     * Exception
     *
     * @param message - message
     */
    public JwtAuthFilterException(final String message) {
        super(message);
    }
}
