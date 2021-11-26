package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import org.apache.tomcat.jni.Local;

import java.util.Locale;
import java.util.UUID;

/**
 * This DTO stores information about actual price of product in UC in region
 * which means ordinary price, discount price and currency symbol
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class ActualProductPriceInRegionDTO {
    private final UUID productId;
    private final String productName;
    private final double normalPrice;
    //should be null when no discount found
    private final Double discountPrice;
    private final Locale requestedRegion;
    private final Locale actualRegion;
}
