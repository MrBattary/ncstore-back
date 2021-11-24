package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.exception.ProductServiceCreationException;
import com.netcracker.ncstore.model.Product;

import java.util.List;

import java.util.UUID;

/**
 * Interface.
 * Useless, but we need it because interfaces are needed in SOLID.
 * Should be used for any service with logic related to Product.
 */
public interface IProductsService {
    /**
     * Returns list of DTOs containing information about
     * product(name, price, discount, currency) and paging data.
     *
     * @param productsGetRequest list containing DTOs which contains needed information
     * @return list of DTOs
     */
    List<ProductsGetResponse> getPageOfProductsUsingFilterAndSortParameters(final ProductsGetRequest productsGetRequest);

    /**
     * Create new product in store with given data.
     *
     * @param productData - ProductDataDTO containing all needed information
     * @return ProductDTO - a DTO representing created entity
     * @throws ProductServiceCreationException - when product could not be created
     */
    ProductDTO createNewProductInStore(final ProductCreateDTO productData) throws ProductServiceCreationException;

    /**
     * Checks if product with provided UUID exists.
     *
     * @param id - UUID of product
     * @return true if products exists
     */
    boolean doesProductExist(UUID id);

    /**
     * Checks if buyers can buy a product with provided UUID
     *
     * For example, product might be out of stock or discontinued
     *
     * @param id - product UUID
     * @return true if product is on sale
     */
    boolean checkIfProductIsOnSale(UUID id);

    /**
     * Returns real product entity by UUID
     *
     * @param id - UUID of product
     * @return product entity with provided UUID
     */
    Product loadProductEntityById(UUID id);
}
