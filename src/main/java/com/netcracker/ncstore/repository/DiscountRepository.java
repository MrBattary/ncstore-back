package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {
}