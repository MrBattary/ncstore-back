package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
}