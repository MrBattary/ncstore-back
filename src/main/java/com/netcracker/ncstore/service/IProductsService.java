package com.netcracker.ncstore.service;

import com.netcracker.ncstore.dto.ProductsGetRequestDTO;
import com.netcracker.ncstore.dto.ProductsGetResponseDTO;

public interface IProductsService {
    public ProductsGetResponseDTO getPageOfProductsByNameAndCategories(ProductsGetRequestDTO productsGetRequestDTO);
}
