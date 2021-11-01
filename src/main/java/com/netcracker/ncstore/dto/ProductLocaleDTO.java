package com.netcracker.ncstore.dto;

import java.util.Locale;
import java.util.UUID;

public class ProductLocaleDTO {
    private final UUID productId;
    private final Locale locale;

    public ProductLocaleDTO(UUID productId, Locale locale) {
        this.productId = productId;
        this.locale = locale;
    }

    public UUID getProductId() {
        return productId;
    }

    public Locale getLocale() {
        return locale;
    }
}
