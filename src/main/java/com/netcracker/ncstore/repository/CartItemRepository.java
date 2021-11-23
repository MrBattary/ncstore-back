package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    @Modifying
    @Query("delete from CartItem c where c.cart.userId = ?1")
    int deleteByCartId(UUID id);
}