package com.netcracker.ncstore.service.role;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;

import java.util.List;

public interface IRoleService {
    /**
     * Creates list of Roles from provided valid role names
     * If roleNames is empty, returns list with the CUSTOMER role
     *
     * @param roleNames - list of role names
     * @return - list of Role objects
     */
    List<Role> parseRoleNamesListToRolesList(List<String> roleNames);


    /**
     * Creates list of roleNames from provided Roles list
     *
     * @param roles - list of Role objects
     * @return - list of role names as ERoleName
     */
    List<ERoleName> rolesListToRoleNamesList(List<Role> roles);
}
