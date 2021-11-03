package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Locale;
import java.util.UUID;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class ProductPriceInRegionDTO {
    private final UUID productId;
    private final String productName;
    private final double normalPrice;
    //should be null when no discount found
    private final Double discountPrice;
    private final Locale locale;
}
