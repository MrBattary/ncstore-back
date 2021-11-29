package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class ProductGetResponse {
    private final UUID productId;
    private final UUID supplierId;
    private final String supplierName;
    private final String productName;
    private final String productDescription;
    private final double normalPrice;
    private final Double discountPrice;
    private final String priceCurrency;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
    private final UUID parentProductId;
    private final List<String> categoriesNames;
}
