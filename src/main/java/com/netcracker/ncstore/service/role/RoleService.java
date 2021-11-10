package com.netcracker.ncstore.service.role;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> buildRolesList(final List<ERoleName> roleNamesList) {
        List<Role> roleList = new ArrayList<>();
        List<String> roleCheckList = new ArrayList<>();
        for (ERoleName roleName : roleNamesList) {
            Role role = roleRepository.getRoleByRoleName(roleName);
            if (role != null && !roleCheckList.contains(role.getRoleName().toString())) {
                roleCheckList.add(role.getRoleName().toString());
                roleList.add(role);
            }
        }

        if (roleList.isEmpty()) {
            roleList.add(roleRepository.getRoleByRoleName(ERoleName.CUSTOMER));
        }

        return roleList;
    }
}
