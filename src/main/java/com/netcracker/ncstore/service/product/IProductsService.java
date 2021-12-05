package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.ProductIdAuthDTO;
import com.netcracker.ncstore.dto.ProductIdUpdateRequestAuthDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.request.ProductGetRequest;
import com.netcracker.ncstore.dto.response.DeleteProductResponse;
import com.netcracker.ncstore.dto.response.GetProductResponse;
import com.netcracker.ncstore.dto.response.ProductGetResponse;
import com.netcracker.ncstore.dto.response.ProductsGetPaginationResponse;
import com.netcracker.ncstore.dto.response.UpdateProductResponse;
import com.netcracker.ncstore.exception.ProductServiceCreationException;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.exception.ProductServiceNotAllowedException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundExpectedException;
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
     * @param productGetRequest list containing DTOs which contains needed information
     * @return list of DTOs
     */
    List<ProductsGetPaginationResponse> getPageOfProductsUsingFilterAndSortParameters(final ProductGetRequest productGetRequest);

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

    /**
     * Returns product public data
     * @param getProductDTO - DTO
     * @return - Product's data as response
     * @throws ProductServiceNotFoundException - if product was not found
     */
    ProductGetResponse getProductResponse(ProductLocaleDTO getProductDTO) throws ProductServiceNotFoundException;

    /**
     * Returns product detailed data for supllier
     * @param productIdAuthDTO - DTO
     * @return - Product's data as response
     * @throws ProductServiceNotFoundException - if product was not found
     * @throws ProductServiceNotAllowedException - if product does not belong to requesting supplier
     */
    GetProductResponse getProductDetailed(ProductIdAuthDTO productIdAuthDTO)
            throws ProductServiceNotFoundException, ProductServiceNotAllowedException;

    /**
     * Update existing product
     * @param productIdUpdateRequestAuthDTO - DTO
     * @return - UpdateProductResponse
     * @throws ProductServiceNotFoundException - if product was not found
     * @throws ProductServiceNotAllowedException - if product does not belong to requesting supplier
     * @throws ProductServiceValidationException - if new data was corrupted
     */
    UpdateProductResponse updateProduct(ProductIdUpdateRequestAuthDTO productIdUpdateRequestAuthDTO)
            throws ProductServiceNotFoundException, ProductServiceNotAllowedException, ProductServiceValidationException;

    /**
     *
     * @param productIdAuthDTO - DTO
     * @return - DeleteProductResponse
     * @throws ProductServiceNotFoundExpectedException - if product was not found
     * @throws ProductServiceNotAllowedException - if product does not belong to requesting supplier
     */
    DeleteProductResponse deleteProduct(ProductIdAuthDTO productIdAuthDTO)
            throws ProductServiceNotFoundExpectedException, ProductServiceNotAllowedException;
}
