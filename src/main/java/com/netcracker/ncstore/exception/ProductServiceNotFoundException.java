package com.netcracker.ncstore.exception;

public class ProductServiceNotFoundException extends RuntimeException {
    public ProductServiceNotFoundException(String s) {
        super(s);
    }

    public ProductServiceNotFoundException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
