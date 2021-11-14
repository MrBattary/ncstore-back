package com.netcracker.ncstore.exception;

/**
 * Should be used when user wants to create product while not having a SUPPLIER role
 */
public class CreatorOfProductNotSupplierException extends RuntimeException {
    public CreatorOfProductNotSupplierException(String message) {
        super(message);
    }

    public CreatorOfProductNotSupplierException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
