package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
public class RegisterPersonRequest {
    private final String email;
    private String password;
    private final String nickName;
    private final String firstName;
    private final String lastName;
    private final LocalDate birthday;
    private final List<String> roles;
}
