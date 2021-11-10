package com.netcracker.ncstore.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

/**
 * Sign up request for person
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
@Setter
public class SignUpPersonRequest {
    private final String email;
    private String password;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final List<ERoleName> roles;
}
