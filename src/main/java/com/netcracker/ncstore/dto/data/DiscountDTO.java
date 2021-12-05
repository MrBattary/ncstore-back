package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Discount;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Contains all information about real Discount entity,
 * as it is represented in database.
 * <p>
 * Used to safely transfer entity data between parts of the Program
 */
@Getter
@AllArgsConstructor
public class DiscountDTO {
    private final UUID id;
    private final double discountPrice;
    private final Instant startUtcTime;
    private final Instant endUtcTime;
    private final UUID productPriceId;

    public DiscountDTO(Discount discount) {
        id = discount.getId();
        discountPrice = discount.getDiscountPrice();
        startUtcTime = discount.getStartUtcTime();
        endUtcTime = discount.getEndUtcTime();
        productPriceId = discount.getProductPrice().getId();
    }
}
