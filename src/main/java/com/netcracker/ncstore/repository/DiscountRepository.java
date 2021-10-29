package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}