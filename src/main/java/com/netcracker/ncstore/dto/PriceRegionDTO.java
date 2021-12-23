package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

/**
 * DTO Used to transfer price and Locale info
 */
@AllArgsConstructor
@Getter
public class PriceRegionDTO {
    private final double price;
    private final Locale region;
}
