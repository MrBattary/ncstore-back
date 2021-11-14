package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.ProductPrice;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.UUID;

/**
 * Contains all information about real ProductPrice entity,
 * as it is represented in database.
 * <p>
 * Used to safely transfer entity data between parts of the Program
 */

@AllArgsConstructor
@Getter
public class ProductPriceDTO {
    private final UUID id;
    private final double price;
    private final Locale locale;
    private final UUID productId;

    public ProductPriceDTO(ProductPrice productPrice) {
        id = productPrice.getId();
        price = productPrice.getPrice();
        locale = productPrice.getLocale();
        productId = productPrice.getProduct().getId();
    }
}
