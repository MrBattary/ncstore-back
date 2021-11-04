package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.ProductsGetRequestDTO;
import com.netcracker.ncstore.dto.ProductsGetResponseDTO;
import com.netcracker.ncstore.exception.ProductsPageNumberExceedsPageCountException;

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
     * @throws ProductsPageNumberExceedsPageCountException - if requested page doesn't exist
     */
    ProductsGetResponseDTO getPageOfProductsByNameAndCategories(ProductsGetRequestDTO productsGetRequestDTO)
    throws ProductsPageNumberExceedsPageCountException;
}
