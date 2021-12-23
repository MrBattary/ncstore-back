package com.netcracker.ncstore.dto.create;

import com.netcracker.ncstore.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.Locale;

@AllArgsConstructor
@Getter
public class DiscountedProductPriceCreateDTO {
    private final Product product;
    private final Locale region;
    private final double regularPrice;
    private final double discountPrice;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
}
