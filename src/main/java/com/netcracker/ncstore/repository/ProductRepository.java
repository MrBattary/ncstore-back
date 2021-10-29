package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}