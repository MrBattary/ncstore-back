package com.netcracker.ncstore.security.filter;

import com.netcracker.ncstore.security.token.UnauthenticatedJwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Abstract filter to take JwtToken from the http request.
 */
public abstract class AJwtAuthFilter extends AbstractAuthenticationProcessingFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String anonymousUserName = "ANONYMOUS";

    private Authentication anonymousToken() {
        return new AnonymousAuthenticationToken(anonymousUserName, anonymousUserName,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+anonymousUserName)));
    }

    /**
     * Constructor
     * @param matcher - matcher type
     */
    AJwtAuthFilter(final RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException {
        String token;
        try {
            token = takeToken(request);
        } catch (Exception e) {
            logger.warn("Failed to get token: " + e.getMessage() + " !");
            return anonymousToken();
        }
        return new UnauthenticatedJwtToken(token);
    }

    /**
     * Take token
     * @param request - web request
     * @return - Token
     * @throws AuthenticationException - Authentication failed
     */
    protected abstract String takeToken(final HttpServletRequest request) throws AuthenticationException;

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication authResult)
            throws IOException, ServletException
    {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}