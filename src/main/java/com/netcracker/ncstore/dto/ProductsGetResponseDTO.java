package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class ProductsGetResponseDTO {
    private final UUID productId;
    private final String productName;
    private final double productPrice;
    private final Double discountPrice;//non-primitive because there may be no discount
    private final String priceCurrency;
}
