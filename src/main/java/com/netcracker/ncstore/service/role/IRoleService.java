package com.netcracker.ncstore.service.role;

import com.netcracker.ncstore.model.Role;

import java.util.List;

public interface IRoleService {
    /**
     * User roles builder
     * If roleNames is empty, returns list with the CUSTOMER role
     *
     * @param roleNames - list of role names
     * @return - List of Role objects
     */
    List<Role> buildRolesList(List<String> roleNames);
}
