package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("select p from Product p join p.categories categories where upper(p.name) like upper(concat('%', ?1, '%')) and categories.id in ?2")
    Page<Product> findProductsByLikeNameAndCategories(String name, Collection<UUID> categoriesIDs, Pageable pageable);

    @Query("select p from Product p left join p.productPrices where upper(p.name) like upper(concat('%', ?1, '%'))")
    Page<Product> findProductByLikeName(String name, Pageable pageable);

    @Query("select p from Product p left join p.productPrices productPrices left join productPrices.discount discount where upper(p.name) like upper(concat('%', ?1, '%')) order by productPrices.price - coalesce(discount.discountPrice,0) asc")
    Page<Product> test(String name, Pageable pageable);




}