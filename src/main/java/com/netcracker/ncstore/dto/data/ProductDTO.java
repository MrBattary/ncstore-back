package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Category;
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
    private final List<UUID> categoriesIds;

    public ProductDTO(Product product) {
        id = product.getId();
        parentProductId = product.getParentProduct() != null ? product.getParentProduct().getId() : null;
        name = product.getName();
        description = product.getDescription();
        supplierId = product.getSupplier().getId();
        productStatus = product.getProductStatus();
        categoriesIds = product.getCategories().
                stream().
                map(Category::getId).
                collect(Collectors.toList());
    }
}
