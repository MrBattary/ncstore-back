package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UserAddRoleRequest {
    private final String email;
    private final String nameOfNewuserRole;
}
