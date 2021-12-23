package com.netcracker.ncstore.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

/**
 * Sign in request with email and password
 */
@AllArgsConstructor
@Getter
public class SignInEmailPasswordBody {
    private final String email;
    private final String password;
}

