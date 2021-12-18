package com.netcracker.ncstore.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class DiscountedProductPriceCreateDTO {
    private final UUID productId;
    private final Locale region;
    private final double regularPrice;
    private final double discountPrice;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
}
