package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("select o from Order o where o.user.email = ?1")
    Page<Order> findByUserEmail(String email, Pageable pageable);

    @Query("select (count(o) > 0) from Order o left outer join OrderItem oi on o.id = oi.order.id WHERE o.user.email = ?1 and oi.product.id = ?2")
    boolean existsProductForUser(String email, UUID productId);
}