package com.netcracker.ncstore.exception;

public class ProductServiceNotAllowedException extends RuntimeException {
    public ProductServiceNotAllowedException(String s) {
        super(s);
    }

    public ProductServiceNotAllowedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
