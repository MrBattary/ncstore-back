package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public class ProductIdLocaleDTO {
    private final String productId;
    private final Locale locale;
}
