package com.netcracker.ncstore.dto.request;

import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;


/**
 * DTO containing information about product that will
 * be added to store.
 */
@AllArgsConstructor
@Getter
public class ProductCreateRequest {
    private final String emailOfIssuer;
    private final String productName;
    private final String productDescription;
    private final List<PriceRegionDTO> normalPrices;
    private final List<DiscountPriceRegionDTO> discountPrices;
    private final UUID parentProductId;
    private final List<String> categoriesNames;
}
