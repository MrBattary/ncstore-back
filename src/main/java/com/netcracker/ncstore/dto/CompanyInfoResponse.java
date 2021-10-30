package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Response on successful user info request
 * with company data
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class CompanyInfoResponse {
    private final String email;
    private final List<String> roles;
    private final Double balance;
    private final String userType;
    private final String companyName;
    private final String description;
    private final String foundationDate;
}
