package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Locale;
import java.util.UUID;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, UUID> {
    @Query("select p from ProductPrice p where p.product.id = ?1 and p.locale = ?2")
    ProductPrice findByProductIDAndLocale(UUID id, Locale locale);

    @Modifying
    @Query("delete from ProductPrice p where p.product.id = ?1")
    int deleteByProductId(UUID id);
}