package com.netcracker.ncstore.dto.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DiscountCreateDTO {
    private final double discountPrice;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
    private final UUID productPriceId;
}
