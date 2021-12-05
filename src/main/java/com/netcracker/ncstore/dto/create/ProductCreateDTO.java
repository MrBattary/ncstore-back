package com.netcracker.ncstore.dto.create;


import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

/**
 * This DTO contains all information needed to
 * create new product instance
 */
@AllArgsConstructor
@Getter
public class ProductCreateDTO {
    private final String name;
    private final String description;
    private final Principal principal;
    private final EProductStatus status;
    private final List<PriceRegionDTO> prices;
    private final List<DiscountPriceRegionDTO> discountPrices;
    private final UUID parentProductUUID;
    private final List<String> categoriesNames;
}
