package com.netcracker.ncstore.service.product.interfaces;

import com.netcracker.ncstore.dto.ProductsPageRequestDTO;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.model.Product;
import org.springframework.data.domain.Page;

import java.util.UUID;

/**
 * Interface for all services which are used for accessing product info
 */
public interface IProductDataService {
    /**
     * Returns page of Products matching provided request
     *
     * @param productsPageRequest DTO containing info about page plus searching and sorting parameters
     * @return Page<Product>
     */
    Page<Product> getPageOfProductsUsingFilterAndSortParameters(final ProductsPageRequestDTO productsPageRequest);

    /**
     * Checks if product with provided UUID exists.
     *
     * @param id - UUID of product
     * @return true if products exists
     */
    boolean doesProductExist(UUID id);

    /**
     * Checks if buyers can buy a product with provided UUID
     * <p>
     * For example, product might be out of stock or discontinued
     *
     * @param id - product UUID
     * @return true if product is on sale
     * @throws ProductServiceNotFoundException when product no found
     */
    boolean checkIfProductIsOnSale(UUID id) throws ProductServiceNotFoundException;

    /**
     * Returns real product entity by UUID
     *
     * @param id - UUID of product
     * @return product entity with provided UUID
     * @throws ProductServiceNotFoundException when product no found
     */
    Product loadProductEntityById(UUID id) throws ProductServiceNotFoundException;
}
