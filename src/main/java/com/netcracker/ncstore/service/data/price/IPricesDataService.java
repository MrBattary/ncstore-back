package com.netcracker.ncstore.service.data.price;

import com.netcracker.ncstore.dto.ActualProductPrice;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.exception.PricesServiceNotFoundException;

public interface IPricesDataService {
    /**
     * Returns price of specified product in specified region.
     * If no price for provided product in specified region exists
     * returns price for default region default
     *
     * @param productLocale DTO containing ProductID and Locale
     * @return ProductPrice
     * @throws PricesServiceNotFoundException if provided product does not exist
     */
    ActualProductPrice getActualPriceForProductInRegion(final ProductLocaleDTO productLocale) throws PricesServiceNotFoundException;
}
