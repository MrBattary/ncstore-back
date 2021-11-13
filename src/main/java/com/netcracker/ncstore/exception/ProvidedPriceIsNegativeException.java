package com.netcracker.ncstore.exception;

/**
 * This exception is used when provided price is negative
 */
public class ProvidedPriceIsNegativeException extends RuntimeException {
    public ProvidedPriceIsNegativeException() {
        super();
    }

    public ProvidedPriceIsNegativeException(String message) {
        super(message);
    }

    public ProvidedPriceIsNegativeException(String message, Throwable cause) {
        super(message, cause);
    }
}
