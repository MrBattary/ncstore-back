package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RegisterUserDTO {
    private final String email;
    private final String password;
    private final List<Role> roles;
}
