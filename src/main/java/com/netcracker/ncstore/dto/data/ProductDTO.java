package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class ProductDTO {
    private final UUID id;
    private final UUID parentProductId;
    private final String name;
    private final String description;
    private final UUID supplierId;
    private final EProductStatus productStatus;
    private final List<CategoryDTO> categoryDTOS;

    public ProductDTO(Product product) {
        id = product.getId();
        parentProductId = product.getParentProduct().getId();
        name = product.getName();
        description = product.getDescription();
        supplierId = product.getSupplier().getId();
        productStatus = product.getProductStatus();
        categoryDTOS = product.getCategories().
                stream().
                map(CategoryDTO::new).
                collect(Collectors.toList());
    }
}
