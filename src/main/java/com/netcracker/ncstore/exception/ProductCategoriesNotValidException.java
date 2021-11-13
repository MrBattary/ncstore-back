package com.netcracker.ncstore.exception;

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
