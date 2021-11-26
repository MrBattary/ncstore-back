package com.netcracker.ncstore.exception;

/**
 * used when provided region is not supported by an application and converter can not convert its price to UC price
 */
public class PriceConversionServiceLocaleNotSpecifiedException extends RuntimeException{
    public PriceConversionServiceLocaleNotSpecifiedException() {
    }

    public PriceConversionServiceLocaleNotSpecifiedException(String message) {
        super(message);
    }

    public PriceConversionServiceLocaleNotSpecifiedException(String message, Throwable cause) {
        super(message, cause);
    }
}
