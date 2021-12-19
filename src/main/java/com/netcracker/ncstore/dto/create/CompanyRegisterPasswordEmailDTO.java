package com.netcracker.ncstore.dto.create;

import com.netcracker.ncstore.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class CompanyRegisterPasswordEmailDTO {
    private final String email;
    private String password;
    private final String companyName;
    private final String description;
    private final LocalDate foundationDate;
    private final List<Role> roles;
}
