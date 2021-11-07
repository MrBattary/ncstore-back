package com.netcracker.ncstore.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Authorization which holds subject and roles/authorities from the JWT token.
 */
public class AuthenticatedJwtToken extends AbstractAuthenticationToken {
    private final String subject;

    /**
     * Constructor
     * @param subject - User email (login)
     * @param grantedAuthorities - Collection of granted authorities
     */
    public AuthenticatedJwtToken(final String subject,
                                 final Collection<GrantedAuthority> grantedAuthorities) {
        super(grantedAuthorities);
        this.subject = subject;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return new Object();
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }

}
