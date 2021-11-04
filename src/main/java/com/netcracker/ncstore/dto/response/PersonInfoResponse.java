package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Response on successful user info request
 * with person data
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class PersonInfoResponse {
    private final String email;
    private final List<String> roles;
    private final Double balance;
    private final String userType;
    private final String userName;
    private final String firstName;
    private final String secondName;
    private final String birthday;
}
