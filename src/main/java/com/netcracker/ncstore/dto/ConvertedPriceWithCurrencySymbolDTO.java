package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@Getter
@AllArgsConstructor
public class ConvertedPriceWithCurrencySymbolDTO {
    private final double price;
    private final String symbol;
    private final Locale locale;
}
