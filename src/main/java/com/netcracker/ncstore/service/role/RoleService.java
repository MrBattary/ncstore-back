package com.netcracker.ncstore.service.role;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final Logger log;

    public RoleService(final RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        log = LoggerFactory.getLogger(RoleService.class);
    }

    @Override
    public List<Role> buildRolesList(final List<String> roleNamesList) {
        List<Role> roleList = new ArrayList<>();
        List<String> roleCheckList = new ArrayList<>();
        for (String roleName : roleNamesList) {
            try {
                Role role = roleRepository.getRoleByRoleName(ERoleName.valueOf(roleName));
                if (!roleCheckList.contains(role.getRoleName().toString())) {
                    roleCheckList.add(role.getRoleName().toString());
                    roleList.add(role);
                }
            } catch (IllegalArgumentException e) {
                log.warn("Role " + roleName + " is not exist");
            }
        }

        if (roleList.isEmpty()) {
            log.info("List of user roles is empty, adding default CUSTOMER role");
            roleList.add(roleRepository.getRoleByRoleName(ERoleName.CUSTOMER));
        }

        return roleList;
    }
}
