package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.ProductIdAuthDTO;
import com.netcracker.ncstore.dto.ProductIdLocaleDTO;
import com.netcracker.ncstore.dto.ProductIdUpdateRequestAuthDTO;
import com.netcracker.ncstore.dto.UserEmailAndRolesDTO;
import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.request.UpdateProductRequest;
import com.netcracker.ncstore.dto.response.GetProductResponse;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.dto.response.UpdateProductResponse;
import com.netcracker.ncstore.exception.ProductServiceCreationException;
import com.netcracker.ncstore.exception.ProductServiceNotAllowedException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.exception.ProductServiceValidationException;

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
     * @param List<ProductsGetResponse> list containing DTOs which contains needed information
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
     * Returns product public data
     * @param getProductDTO - DTO
     * @return - Product's data as response
     * @throws ProductServiceNotFoundException - if product was not found
     */
    GetProductResponse getProductByProductId(ProductIdLocaleDTO getProductDTO) throws ProductServiceNotFoundException;

    /**
     * Returns product detailed data for supllier
     * @param productIdAuthDTO - DTO
     * @return - Product's data as response
     * @throws ProductServiceNotFoundException - if product was not found
     * @throws ProductServiceNotAllowedException - if product does not belong to requesting supplier
     */
    GetProductResponse getProductDetailedByProductId(ProductIdAuthDTO productIdAuthDTO)
            throws ProductServiceNotFoundException, ProductServiceNotAllowedException;

    /**
     * Update existing product
     * @param productIdUpdateRequestAuthDTO - DTO
     * @return - UpdateProductResponse
     * @throws ProductServiceNotFoundException  - if product was not found
     * @throws ProductServiceNotAllowedException  - if product does not belong to requesting supplier
     * @throws ProductServiceValidationException - if new data was corrupted
     */
    UpdateProductResponse updateProduct(ProductIdUpdateRequestAuthDTO productIdUpdateRequestAuthDTO)
            throws ProductServiceNotFoundException, ProductServiceNotAllowedException, ProductServiceValidationException;
}
