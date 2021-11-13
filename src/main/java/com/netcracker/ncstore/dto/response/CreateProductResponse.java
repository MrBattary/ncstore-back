package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;

/**
 * This DTO contains all create
 * product response information
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class CreateProductResponse {
    private final UUID productId;
    private final UUID supplierId;
    private final String productName;
    private final String productDescription;
    private final List<PriceRegionDTO> regionalPrices;
    private final UUID parentProductId;
    private final List<String> categoriesNames;
}