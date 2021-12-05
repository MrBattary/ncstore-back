package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Locale;

/**
 * DTO Used to transfer price and Locale info
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class PriceRegionDTO {
    private final double price;
    private final Locale region;
}
