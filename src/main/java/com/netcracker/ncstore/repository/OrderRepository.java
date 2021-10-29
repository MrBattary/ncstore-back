package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
}