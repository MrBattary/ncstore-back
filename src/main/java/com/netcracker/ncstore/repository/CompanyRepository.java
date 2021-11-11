package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
    @Query("SELECT c FROM Company c WHERE c.user.email = :email")
    Company findCompanyByUserEmail(@Param("email") String email);
}