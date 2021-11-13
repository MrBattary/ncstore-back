package com.netcracker.ncstore.exception;

public class CreatorOfProductNotSupplierException extends RuntimeException {
    public CreatorOfProductNotSupplierException(String message) {
        super(message);
    }

    public CreatorOfProductNotSupplierException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
