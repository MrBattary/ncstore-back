package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}