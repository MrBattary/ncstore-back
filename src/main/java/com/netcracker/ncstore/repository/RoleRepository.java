package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}