package com.netcracker.ncstore.security;

import com.netcracker.ncstore.dto.UserLoginAndRolesDTO;
import com.netcracker.ncstore.security.token.AuthenticatedJwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to generate and parse JWT tokens.
 */
@Service
public class JsonJwtTokenService implements IJwtTokenService {
    private final JwtSettings settings;
    private final String authorities;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructor
     * @param settings - token settings
     */
    public JsonJwtTokenService(final JwtSettings settings) {
        this.settings = settings;
        this.authorities = "authorities";
    }

    @Override
    public String createToken(final UserLoginAndRolesDTO userLoginAndRolesDTO) {
        logger.info("Generating token for user {}", userLoginAndRolesDTO.getEmail());

        Instant now = Instant.now();

        Claims claims = Jwts.claims()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(userLoginAndRolesDTO.getEmail())
                .setExpiration(Date.from(now.plus(settings.getTokenExpiredIn())));
        claims.put(authorities, userLoginAndRolesDTO.getUserRoleNames());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .compact();
    }

    @Override
    public Duration getTokenExpiredIn() {
        return settings.getTokenExpiredIn();
    }

    @Override
    public Authentication parseToken(final String token) {
        Jws<Claims> claims = Jwts.parser().setSigningKey(settings.getTokenSigningKey()).parseClaimsJws(token);

        String subject = claims.getBody().getSubject();
        List<String> tokenAuthorities = claims.getBody().get(authorities, List.class);
        List<GrantedAuthority> grantedAuthorities = tokenAuthorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new AuthenticatedJwtToken(subject, grantedAuthorities);
    }
}
