package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.ActualProductPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.model.ProductPrice;

import java.util.List;
import java.util.UUID;

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
    ActualProductPriceWithCurrencySymbolDTO getActualPriceForProductInRegion(ProductLocaleDTO productLocale);

    /**
     * Creates new instance of ProductPrice based on provided parameters.
     *
     * @param productPriceCreateDTO - ProductPriceDataDTO
     * @return ProductPriceDTO which represents created ProductPrice in a safe way
     * @throws PricesServiceValidationException - when price data is not valid
     */
    ProductPriceDTO createProductPrice(ProductPriceCreateDTO productPriceCreateDTO) throws PricesServiceValidationException;

    /**
     * Returns all prices for specified product in DTO
     *
     * @param productId - product UUID
     * @return ProductPriceDTO
     */
    List<ProductPriceDTO> getPricesForProduct(UUID productId);

    /**
     * Deletes all prices and related discount prices
     * @param productPrices - list of ProductPrice
     */
    void deleteAllProvidedPrices(List<ProductPrice> productPrices);
}
