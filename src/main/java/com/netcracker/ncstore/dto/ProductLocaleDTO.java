package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductLocaleDTO {
    private final UUID productId;
    private final Locale locale;
}
