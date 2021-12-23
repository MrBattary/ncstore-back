package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductReviewRepository extends JpaRepository<ProductReview, UUID> {
    @Query("select (count(p) > 0) from ProductReview p where p.author.id = ?1")
    boolean existsByAuthorId(UUID id);
}