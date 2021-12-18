package com.netcracker.ncstore.exception;

public class ProductServicePermissionException extends RuntimeException {
    public ProductServicePermissionException(String s) {
        super(s);
    }

    public ProductServicePermissionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
