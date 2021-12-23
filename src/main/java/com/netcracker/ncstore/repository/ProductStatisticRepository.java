package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.ProductStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductStatisticRepository extends JpaRepository<ProductStatistic, UUID> {
}