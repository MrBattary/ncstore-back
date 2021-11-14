package com.netcracker.ncstore.exception;

/**
 * Should be used when parent product for product is requested,
 * but this product has no parent (null in database)
 */
public class ParentProductNotFoundException extends RuntimeException {
    public ParentProductNotFoundException() {
        super();
    }

    public ParentProductNotFoundException(String message) {
        super(message);
    }
}
