package com.netcracker.ncstore.service.price.interfaces;

import com.netcracker.ncstore.dto.ProductPricesPopulateProductDTO;
import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.create.DiscountedProductPriceCreateDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.ProductPrice;

import java.util.List;
import java.util.UUID;

/**
 * Interfaces for all services which perform CRUD business logic for ProductPrice and Discount entities
 */
public interface IPricesBusinessService {
    /**
     * Creates new ProductPrice entity based on provided parameters.
     *
     * @param productPriceCreateDTO DTO containing info for crating new entity
     * @return created ProductPrice entity
     * @throws PricesServiceValidationException when provided data for price is invalid
     */
    ProductPrice createProductPrice(final ProductPriceCreateDTO productPriceCreateDTO)
            throws PricesServiceValidationException;

    /**
     * Creates new Discount entity based on provided parameters.
     *
     * @param discountCreateDTO DTO containing info for crating new entity
     * @return created Discount entity
     * @throws PricesServiceValidationException when provided data for discount is invalid
     */
    Discount createDiscountForProduct(final DiscountCreateDTO discountCreateDTO)
            throws PricesServiceValidationException;

    /**
     * Creates new ProductPrice entity and new Discount entity based on provided parameters.
     *
     * @param discountedProductPriceCreateDTO DTO containing info for crating new product price and discount entities
     * @return created ProductPrice entity. Discount entity is mapped for ProductPrice entity using One-To-One.
     * @throws PricesServiceValidationException when provided data is invalid
     */
    ProductPrice createDiscountedProductPrice(final DiscountedProductPriceCreateDTO discountedProductPriceCreateDTO)
            throws PricesServiceValidationException;

    /**
     * Populates product with provided prices.
     *
     * @param dto DTO containing product and lists of regular and discount prices
     * @return List<ProductPrice> containing created prices
     * @throws PricesServiceValidationException when provided data is invalid
     */
    List<ProductPrice> populateProductWithPrices(final ProductPricesPopulateProductDTO dto)
            throws PricesServiceValidationException;

    void deleteProductPrice(ProductPrice productPrice);

    void deleteAllProductPricesForProduct(UUID productId);
}
