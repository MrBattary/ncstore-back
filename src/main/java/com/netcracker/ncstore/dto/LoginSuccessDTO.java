package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginSuccessDTO {
    private final String token;
    private final User user;
}
