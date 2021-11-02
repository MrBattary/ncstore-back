package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}