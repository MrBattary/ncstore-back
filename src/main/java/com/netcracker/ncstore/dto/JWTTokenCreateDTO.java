package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.enumerations.ERoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * User dto contains only userEmail(aka login) and list of authorities names
 */
@Getter
@AllArgsConstructor
public class JWTTokenCreateDTO {
    private final String email;
    private final List<ERoleName> roles;

    public List<String> getUserRoleNames() {
        List<String> stringList = new ArrayList<>(roles.size());
        for (ERoleName roleName : roles) {
            stringList.add(roleName.toString());
        }
        return stringList;
    }
}
