package com.netcracker.ncstore.exception;

/**
 * Should be used when provided list of categories for product is invalid.
 * Which means, list is empty or even null
 */
public class ProductCategoriesNotValidException extends RuntimeException {
    public ProductCategoriesNotValidException() {
    }

    public ProductCategoriesNotValidException(String message) {
        super(message);
    }

    public ProductCategoriesNotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
