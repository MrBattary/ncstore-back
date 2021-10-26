package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Class that defines a discount for a specific product price.
 * Discounted price is always a temporary price.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discount {
    private long productPriceId;
    private double discountPrice;
    private Instant startInstant;
    private Instant endInstant;
}
