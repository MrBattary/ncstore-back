package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChangePasswordDTO {
    private final String oldPassword;
    private final String newPassword;
    private final String email;
}
