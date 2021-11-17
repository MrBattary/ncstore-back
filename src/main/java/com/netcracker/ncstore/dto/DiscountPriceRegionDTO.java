package com.netcracker.ncstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.Locale;

/**
 * DTO used for transferring information about discount
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class DiscountPriceRegionDTO {
    private final double price;
    private final Locale region;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
}
