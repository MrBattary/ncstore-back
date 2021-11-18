package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.model.enumerations.EUserType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserTypeEmailPasswordRolesDTO {
    private final UUID userId;
    private final EUserType type;
    private final String email;
    private final String password;
    private final List<ERoleName> roles;
}
