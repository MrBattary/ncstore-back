package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.enumerations.ERoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserModelWithoutIdDTO {
    private final String email;
    private final String password;
    private final double balance;
    private final List<ERoleName> roleNames;
}
