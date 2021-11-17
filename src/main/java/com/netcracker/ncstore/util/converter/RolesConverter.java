package com.netcracker.ncstore.util.converter;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class RolesConverter {
    public static List<ERoleName> rolesListToRoleNamesList(List<Role> rolesList) {
        return rolesList.
                stream().
                map(Role::getRoleName).
                collect(Collectors.toList());
    }
}
