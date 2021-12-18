package com.netcracker.ncstore.dto.create;

import com.netcracker.ncstore.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class DiscountCreateDTO {
    private final Product product;
    private final Locale region;
    private final double discountPrice;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
}
