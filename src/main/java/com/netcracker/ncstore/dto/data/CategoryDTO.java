package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Category;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class CategoryDTO {
    private final UUID id;
    private final String name;
    private final List<ProductDTO> productDTOS;

    public CategoryDTO(Category category) {
        id = category.getId();
        name = category.getName();
        productDTOS = category.getProducts().
                stream().
                map(ProductDTO::new).
                collect(Collectors.toList());
    }
}
