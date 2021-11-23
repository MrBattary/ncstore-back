package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID> {

}