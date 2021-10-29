package com.netcracker.ncstore.util.either;

/**
 * Exception for Either class, throws if setting/getting null value
 * @see Either
 */
public class NullEitherException extends RuntimeException {
    /**
     * Exception
     *
     * @param message - message
     */
    public NullEitherException(final String message) {
        super(message);
    }

    /**
     * Trace exception
     *
     * @param message - message
     * @param e       - exception
     */
    public NullEitherException(final String message, final Exception e) {
        super(message, e);
    }
}
