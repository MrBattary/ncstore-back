package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductUpdateDTO {
    private final UUID productId;
    private final User issuer;
    private final String name;
    private final String description;
    private final EProductStatus status;
    private final List<PriceRegionDTO> prices;
    private final List<DiscountPriceRegionDTO> discountPrices;
    private final UUID getParentProductId;
    private final List<String> categoriesNames;
}
