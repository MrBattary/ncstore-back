package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.PriceConversionRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PriceConversionRateRepository extends JpaRepository<PriceConversionRate, UUID> {
}