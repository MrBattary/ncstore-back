package com.netcracker.ncstore.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.UUID;


/**
 * This DTO contains all POST request
 * information for creating product
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class CreateProductRequest {
    private final String productName;
    private final String productDescription;
    private final List<PriceRegionDTO> regionalPrices;
    private final UUID parentProductId;
    private final List<String> categoriesNames;
}
