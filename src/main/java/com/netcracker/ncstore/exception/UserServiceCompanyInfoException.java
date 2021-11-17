package com.netcracker.ncstore.exception;

/**
 * Used when there is some exception while trying to get user's company info
 */
public class UserServiceCompanyInfoException extends RuntimeException{
    public UserServiceCompanyInfoException() {
        super();
    }

    public UserServiceCompanyInfoException(String message) {
        super(message);
    }

    public UserServiceCompanyInfoException(String message, Throwable cause) {
        super(message, cause);
    }
}
