package com.netcracker.ncstore.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * Authorization which holds unparsed and unverified JWT token.
 */
public class UnauthenticatedJwtToken extends AbstractAuthenticationToken {
    private final String token;

    /**
     * Constructor
     * @param token - Token
     */
    public UnauthenticatedJwtToken(final String token) {
        super(null);
        this.token = token;
        super.setAuthenticated(false);
    }

    @Override
    public void setAuthenticated(final boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Error: Cannot set this token to trusted!");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
