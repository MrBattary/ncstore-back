package com.netcracker.ncstore.security.filter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filter to take JwtToken from the request header
 */
public class HeaderJwtAuthFilter extends AJwtAuthFilter {
    private static final Pattern BEARER_AUTH_PATTERN = Pattern.compile("^Bearer\\s+(.*)$");
    private static final int TOKEN_GROUP = 1;

    /**
     * Constructor
     * @param matcher - matcher type
     */
    public HeaderJwtAuthFilter(final RequestMatcher matcher) {
        super(matcher);
    }

    @Override
    protected String takeToken(final HttpServletRequest request) throws AuthenticationException {
        String authHeader = request.getHeader("Authorization");
        Matcher m = BEARER_AUTH_PATTERN.matcher(authHeader);
        if (m.matches()) {
            return m.group(TOKEN_GROUP);
        } else {
            throw new JwtAuthFilterException("Error: Invalid authorization header: " + authHeader + " !");
        }
    }

}