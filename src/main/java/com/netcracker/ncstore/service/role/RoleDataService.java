package com.netcracker.ncstore.service.role;

import com.netcracker.ncstore.exception.RoleServiceNotFoundException;
import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.RoleRepository;
import com.netcracker.ncstore.service.role.interfaces.IRoleDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RoleDataService implements IRoleDataService {
    private final RoleRepository roleRepository;

    public RoleDataService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(ERoleName name) throws RoleServiceNotFoundException {
        return roleRepository.
                findByRoleName(name).
                orElseThrow(() -> new RoleServiceNotFoundException("Role with name " + name.toString() + " not found. "));
    }

    @Override
    public Role getRoleByName(String name) throws RoleServiceNotFoundException {
        return getRoleByName(ERoleName.valueOf(name));
    }
}
