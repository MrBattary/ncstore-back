package com.netcracker.ncstore.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Settings to the JWT token.
 */
@Component
public class JwtSettings {
    private final String tokenIssuer;
    private final String tokenSigningKey;
    private final int aTokenDuration;

    /**
     * Settings constructor
     *
     * @param tokenIssuer     - name
     * @param tokenSigningKey - key
     * @param aTokenDuration  - duration in minutes
     */
    public JwtSettings(@Value("${jwt.issuer}") final String tokenIssuer,
                       @Value("${jwt.signingKey}") final String tokenSigningKey,
                       @Value("${jwt.aTokenDuration}") final int aTokenDuration) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSigningKey = tokenSigningKey;
        this.aTokenDuration = aTokenDuration;
    }

    public String getTokenIssuer() {
        return tokenIssuer;
    }

    public byte[] getTokenSigningKey() {
        return tokenSigningKey.getBytes(StandardCharsets.UTF_8);
    }

    public Duration getTokenExpiredIn() {
        return Duration.ofMinutes(aTokenDuration);
    }

}
