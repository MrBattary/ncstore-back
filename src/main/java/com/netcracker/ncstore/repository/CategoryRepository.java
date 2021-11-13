package com.netcracker.ncstore.repository;

import com.netcracker.ncstore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    @Query("select c from Category c where upper(c.name) = upper(?1)")
    Category findByName(String name);

}