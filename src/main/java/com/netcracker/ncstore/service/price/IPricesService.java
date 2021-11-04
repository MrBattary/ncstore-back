package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;

/**
 * Interface.
 * Useless, but we need it because interfaces are needed in SOLID.
 * Should be used for any service with logic related to ProductPrice.
 */
public interface IPricesService {
    /**
     * Returns productID, productName, productPrice, discountPrice and Locale
     * for a specified ProductID and Locale pair.
     * Discount price should be null, if there is no discount.
     *
     * @param productLocale - ProductID and Locale pair
     * @return ProductPriceInRegionDTO
     */
    ProductPriceInRegionDTO getPriceForProductInRegion(ProductLocaleDTO productLocale);
}
