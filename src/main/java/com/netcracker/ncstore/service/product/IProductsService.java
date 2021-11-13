package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.CreateProductResponse;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.exception.ProductCreationException;

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
     * @param productsGetRequest dto containing needed information
     * @return list of DTOs
     */
    ProductsGetResponse getPageOfProductsByNameAndCategories(final ProductsGetRequest productsGetRequest);

    /**
     * Create new product in store with given data.
     *
     * @param productData - ProductDataDTO containing all needed information
     * @return ProductDTO - a DTO representing created entity
     * @throws ProductCreationException - when product could not be created
     */
    ProductDTO createNewProductInStore(final ProductCreateDTO productData) throws ProductCreationException;
}
