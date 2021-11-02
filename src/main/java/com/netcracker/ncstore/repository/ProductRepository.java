package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("select p from Product p join p.categories categories where upper(p.name) like upper(concat('%', ?1, '%')) and categories.id in ?2")
    Page<Product> findProductsByNameAndCategories(String name, Collection<UUID> ids, Pageable pageable);

}