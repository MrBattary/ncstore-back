package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Role;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findRoleByRoleName(ERoleName roleName);
}