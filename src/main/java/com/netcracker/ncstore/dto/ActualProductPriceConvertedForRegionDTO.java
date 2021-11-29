package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Locale;
import java.util.UUID;

/**
 * This DTO stores information about actual price of product in region
 * converted for that region's currency
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class ActualProductPriceConvertedForRegionDTO {
    private final UUID productId;
    private final String productName;
    private final double normalConvertedPrice;
    //should be null when no discount found
    private final Double discountConvertedPrice;
    private final Locale region;
    private final String currencySymbol;
}
