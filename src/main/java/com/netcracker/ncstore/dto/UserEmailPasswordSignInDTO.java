package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserEmailPasswordSignInDTO {
    private final String email;
    private final String password;
}
