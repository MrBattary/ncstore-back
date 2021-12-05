package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.model.enumerations.EUserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDate;
import java.util.List;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class PersonInfoResponse {
    private final EUserType userType;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final List<ERoleName> roles;
}
