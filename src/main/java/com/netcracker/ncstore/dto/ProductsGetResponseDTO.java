package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class ProductsGetResponseDTO {
    private final List<ProductPriceInRegionDTO> productsWithPrices;
    private final int currentPageNumber;
    private final int totalPageCount;
}
