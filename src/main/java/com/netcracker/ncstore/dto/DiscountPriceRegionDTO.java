package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Locale;

/**
 * DTO used for transferring information about discount
 */
@AllArgsConstructor
@Getter
public class DiscountPriceRegionDTO {
    private final double price;
    private final Locale region;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
}
