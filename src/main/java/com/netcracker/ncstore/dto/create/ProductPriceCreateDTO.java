package com.netcracker.ncstore.dto.create;

import com.netcracker.ncstore.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

/**
 * This DTO stores information needed for creating new ProductPrice
 */
@AllArgsConstructor
@Getter
public class ProductPriceCreateDTO {
    private final double price;
    private final Locale region;
    private final Product product;
}
