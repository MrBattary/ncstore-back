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
    Page<Product> findProductsByLikeNameAndCategories(String name, Collection<UUID> categoriesIDs, Pageable pageable);

    @Query("select p from Product p where upper(p.name) like upper(concat('%', ?1, '%'))")
    Page<Product> findProductByLikeName(String name, Pageable pageable);
}