package com.netcracker.ncstore.service.role;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;

import java.util.List;

public interface IRoleService {
    /**
     * User roles builder
     * If roleNames is empty, returns list with the CUSTOMER role
     *
     * @see ERoleName
     * @param roleNames - list of role names
     * @return - List of Role objects
     */
    List<Role> buildRolesList(List<ERoleName> roleNames);
}
