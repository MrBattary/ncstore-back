package com.netcracker.ncstore.service;

import com.netcracker.ncstore.dto.ProductsGetRequestDTO;
import com.netcracker.ncstore.dto.ProductsGetResponseDTO;

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
     * @param productsGetRequestDTO dto containing needed information
     * @return list of DTOs
     */
    public ProductsGetResponseDTO getPageOfProductsByNameAndCategories(ProductsGetRequestDTO productsGetRequestDTO);
}
