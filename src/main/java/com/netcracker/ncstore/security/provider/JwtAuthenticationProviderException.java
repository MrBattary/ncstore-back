package com.netcracker.ncstore.security.provider;

public class JwtAuthenticationProviderException extends RuntimeException {
    /**
     * Trace exception
     *
     * @param message - message
     * @param e       - exception
     */
    public JwtAuthenticationProviderException(final String message, final Exception e) {
        super(message, e);
    }
}
