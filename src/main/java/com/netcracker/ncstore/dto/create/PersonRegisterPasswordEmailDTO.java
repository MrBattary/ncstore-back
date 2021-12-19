package com.netcracker.ncstore.dto.create;

import com.netcracker.ncstore.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class PersonRegisterPasswordEmailDTO {
    private final String email;
    private final String password;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final List<Role> roles;
}
