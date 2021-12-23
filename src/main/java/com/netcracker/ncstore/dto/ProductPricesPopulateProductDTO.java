package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProductPricesPopulateProductDTO {
    private final List<PriceRegionDTO> regularPrices;
    private final List<DiscountPriceRegionDTO> discountPrices;
    private final Product product;
}
