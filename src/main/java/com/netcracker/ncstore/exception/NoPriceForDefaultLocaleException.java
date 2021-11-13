package com.netcracker.ncstore.exception;

/**
 * Used when there is no price for default locale in provided prices list for creating product
 */
public class NoPriceForDefaultLocaleException extends RuntimeException{
    public NoPriceForDefaultLocaleException() {
        super();
    }

    public NoPriceForDefaultLocaleException(String message) {
        super(message);
    }

    public NoPriceForDefaultLocaleException(String message, Throwable cause) {
        super(message, cause);
    }
}
