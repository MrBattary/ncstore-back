package com.netcracker.ncstore.service.role.interfaces;

import com.netcracker.ncstore.exception.RoleServiceNotFoundException;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;

/**
 * Interacts with repositories of roles
 * Responsible for operations with Role
 */
public interface IRoleDataService {
    /**
     * Returns role entity by its enum name
     *
     * @param name enum name of role
     * @return Role
     * @throws RoleServiceNotFoundException
     */
    Role getRoleByName(ERoleName name) throws RoleServiceNotFoundException;

    /**
     * Returns role entity by its string name
     *
     * @param name string name of role
     * @return Role
     * @throws RoleServiceNotFoundException
     */
    Role getRoleByName(String name) throws RoleServiceNotFoundException;
}
