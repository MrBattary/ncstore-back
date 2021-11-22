package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.dto.ActualProductPriceWithCurrencySymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@AllArgsConstructor
public class ProductsGetResponse {
    private final UUID productId;
    private final String productName;
    private final UUID supplierId;
    private final String supplierName;
    private final double normalPrice;
    private final Double discountPrice;
    private final String priceCurrency;
}
