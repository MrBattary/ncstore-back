package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class RegisterCompanyRequest {
    private final String email;
    private final String password;
    private final String companyName;
    private final LocalDate foundationDate;
    private final List<String> roles;
}
