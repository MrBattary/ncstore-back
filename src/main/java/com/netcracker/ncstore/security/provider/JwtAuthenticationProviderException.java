package com.netcracker.ncstore.security.provider;

import org.springframework.security.access.AccessDeniedException;

public class JwtAuthenticationProviderException extends AccessDeniedException {
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
