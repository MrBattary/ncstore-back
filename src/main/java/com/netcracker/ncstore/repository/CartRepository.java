package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {
    @Query("select c from Cart c where c.user.email = ?1")
    Optional<Cart> findByUserEmail(String email);
}