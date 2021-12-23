package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Sign in request with email and password
 */
@AllArgsConstructor
@Getter
public class SignInEmailPasswordBody {
    private final String email;
    private final String password;
}

