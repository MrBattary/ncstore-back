package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;

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
    ProductsGetResponse getPageOfProductsUsingFilterAndSortParameters(ProductsGetRequest productsGetRequest);
}
