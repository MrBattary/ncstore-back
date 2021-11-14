package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("select c from Category c where upper(c.name) = upper(?1)")
    Category findByName(String name);

    @Query("select c from Category c left join c.products products where products.id = ?1")
    List<Category> findByProductId(UUID id);
}