package com.netcracker.ncstore.service.price.interfaces;

import com.netcracker.ncstore.dto.ActualProductPrice;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.create.DiscountedProductPriceCreateDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.data.DiscountDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.exception.DiscountServiceNotFoundException;
import com.netcracker.ncstore.exception.PricesServiceCreationException;
import com.netcracker.ncstore.exception.PricesServiceNotFoundException;
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
     * @throws PricesServiceCreationException when it is impossible to create product price with provided data
     */
    ProductPrice createProductPrice(final ProductPriceCreateDTO productPriceCreateDTO) throws PricesServiceCreationException;

    /**
     * Creates new Discount entity based on provided parameters.
     *
     * @param discountCreateDTO  DTO containing info for crating new entity
     * @return created Discount entity
     * @throws PricesServiceCreationException when it is impossible to create disocunt with provided data
     */
    Discount createDiscountForProduct(final DiscountCreateDTO discountCreateDTO) throws PricesServiceCreationException;

    /**
     * Creates new ProductPrice entity and new Discount entity based on provided parameters.
     *
     * @param discountedProductPriceCreateDTO DTO containing info for crating new product price and discount entities
     * @return created ProductPrice entity. Discount entity is mapped for ProductPrice entity using One-To-One.
     * @throws PricesServiceCreationException when it is impossible to create product price and discount entities with provided data
     */
    ProductPrice createDiscountedProductPrice(final DiscountedProductPriceCreateDTO discountedProductPriceCreateDTO) throws PricesServiceCreationException;

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

    void deleteProductPrice(ProductPrice productPrice);
    void deleteAllProductPricesForProduct(UUID productId);

    //TODO delete this useless method when products service + product controller refactored
    List<ProductPriceDTO> getPricesForProduct(UUID productId);
    //TODO delete this useless method when products service + product controller refactored
    DiscountDTO getDiscountForPrice(final UUID productPriceId) throws DiscountServiceNotFoundException;
}
