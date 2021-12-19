package com.netcracker.ncstore.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;


@AllArgsConstructor
@Getter
public class ProductUpdateRequest {
    private final UUID productId;
    private final String emailOfIssuer;
    private final String productName;
    private final String productDescription;
    private final List<PriceRegionDTO> normalPrices;
    private final List<DiscountPriceRegionDTO> discountPrices;
    private final UUID parentProductId;
    private final List<String> categoriesNames;
}
