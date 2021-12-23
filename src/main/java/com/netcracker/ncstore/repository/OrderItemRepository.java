package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    @Query("select count(o) from OrderItem o where o.product.id = ?1")
    long countProductOccurrences(UUID productId);

    @Query("select coalesce(sum(o.priceUc),0) from OrderItem o where o.product.id = ?1")
    double getTotalUcSalesForProduct(UUID productId);
}