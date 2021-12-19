package com.netcracker.ncstore.exception;

/**
 * Used when some error happens while adding money to user's balance
 */
public class UserServiceBalancePaymentException extends RuntimeException {
    public UserServiceBalancePaymentException() {
    }

    public UserServiceBalancePaymentException(String message) {
        super(message);
    }

    public UserServiceBalancePaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
