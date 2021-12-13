package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

/**
 * DTO containing all fields for PUT /cart response
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class CartItemResponse {
    private final UUID productId;
    private final Integer productCount;
    private final String productName;
    private final double normalPrice;
    private final Double discountPrice;
    private final String priceCurrency;
}
