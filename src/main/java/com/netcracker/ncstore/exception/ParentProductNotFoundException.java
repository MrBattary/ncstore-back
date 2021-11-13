package com.netcracker.ncstore.exception;

public class ParentProductNotFoundException extends RuntimeException{
    public ParentProductNotFoundException() {
        super();
    }

    public ParentProductNotFoundException(String message) {
        super(message);
    }
}
