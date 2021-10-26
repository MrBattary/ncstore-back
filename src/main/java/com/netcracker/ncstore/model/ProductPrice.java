package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

/**
 * Class that defines a price of a specific product in specific region.
 * Needed because prices could be different for poor and rich countries.
 * Needed because prices are in different currencies.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductPrice {
    private long id;
    private long productId;
    private double price;
    private Locale locale;
}
