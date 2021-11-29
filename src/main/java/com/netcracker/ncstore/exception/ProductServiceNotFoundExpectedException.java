package com.netcracker.ncstore.exception;

public class ProductServiceNotFoundExpectedException extends RuntimeException {
    public ProductServiceNotFoundExpectedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
