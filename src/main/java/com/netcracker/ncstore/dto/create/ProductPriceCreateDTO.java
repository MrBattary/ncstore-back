package com.netcracker.ncstore.dto.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Locale;

/**
 * This DTO stores information needed for creating new ProductPrice
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class ProductPriceCreateDTO {
    private final double price;
    private final Locale region;
    private final Product product;
}
