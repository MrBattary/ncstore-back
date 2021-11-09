package com.netcracker.ncstore.security.provider;

import com.netcracker.ncstore.security.IJwtTokenService;
import com.netcracker.ncstore.security.token.UnauthenticatedJwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Authentication provider which is able to verify JWT tokens.
 */
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final IJwtTokenService tokenService;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Constructor
     * @param tokenService - Token service
     */
    public JwtAuthenticationProvider(final IJwtTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException{
        String token = String.valueOf(authentication.getCredentials());
        logger.info("BEFORE REQUEST: Authenticating {}", token);

        try {
            return tokenService.parseToken(token);
        } catch (Exception e) {
            throw new JwtAuthenticationProviderException("Exception: Invalid token received!", e);
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return (UnauthenticatedJwtToken.class.isAssignableFrom(authentication));
    }

}
