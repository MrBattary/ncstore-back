package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserChangePasswordRequest {
    private final String oldPassword;
    private final String newPassword;
    private final String email;
}
