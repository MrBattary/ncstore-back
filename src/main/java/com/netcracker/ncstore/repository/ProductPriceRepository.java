package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
}