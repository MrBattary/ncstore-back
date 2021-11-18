package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.model.enumerations.EUserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

/**
 * Response on successful sign in request
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class SignInResponse {
    private final UUID userId;
    private final EUserType type;
    private final List<ERoleName> roles;
    private final String token;
}
