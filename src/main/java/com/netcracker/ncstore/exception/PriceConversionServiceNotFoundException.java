package com.netcracker.ncstore.exception;

/**
 * Used when something is not found in price conversion service
 */
public class PriceConversionServiceNotFoundException extends RuntimeException {

    public PriceConversionServiceNotFoundException() {
    }

    public PriceConversionServiceNotFoundException(String message) {
        super(message);
    }

    public PriceConversionServiceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
