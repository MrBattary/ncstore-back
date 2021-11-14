package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.model.Product;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contains all information about real Category entity,
 * as it is represented in database.
 * <p>
 * Used to safely transfer entity data between parts of the Program
 */
@Getter
public class CategoryDTO {
    private final UUID id;
    private final String name;
    private final List<UUID> productsIds;

    public CategoryDTO(Category category) {
        id = category.getId();
        name = category.getName();
        productsIds = category.getProducts().
                stream().
                map(Product::getId).
                collect(Collectors.toList());
    }
}
