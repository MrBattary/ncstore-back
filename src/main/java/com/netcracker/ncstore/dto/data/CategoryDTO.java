package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.model.Product;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
