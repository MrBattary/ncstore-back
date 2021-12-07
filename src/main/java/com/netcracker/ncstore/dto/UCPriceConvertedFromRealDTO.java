package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public class UCPriceConvertedFromRealDTO {
    private final double UCAmount;
    private final Locale requestedLocale;
    private final Locale actualLocale;
}
