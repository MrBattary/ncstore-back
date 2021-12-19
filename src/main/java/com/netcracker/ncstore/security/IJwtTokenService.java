package com.netcracker.ncstore.security;

import com.netcracker.ncstore.dto.JWTTokenCreateDTO;
import org.springframework.security.core.Authentication;

import java.time.Duration;

/**
 * Token service interface
 */
public interface IJwtTokenService {
    /**
     * Getter for token duration
     * @return - token duration
     */
    Duration getTokenExpiredIn();
    /**
     * Parses the token
     * @param token - the token string to parse
     * @return - authenticated data
     */
    Authentication parseToken(String token);

    /**
     * Creates new Token for user
     * @param userLoginAndRolesDTO - dto
     * @return - signed token
     */
    String createToken(JWTTokenCreateDTO userLoginAndRolesDTO);
}
