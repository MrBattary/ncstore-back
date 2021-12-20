package com.netcracker.ncstore.service.product.interfaces;

import com.netcracker.ncstore.dto.request.ProductCreateRequest;
import com.netcracker.ncstore.dto.request.ProductDeleteRequest;
import com.netcracker.ncstore.dto.request.ProductGetDetailedRequest;
import com.netcracker.ncstore.dto.request.ProductGetInfoRequest;
import com.netcracker.ncstore.dto.request.ProductGetPaginationRequest;
import com.netcracker.ncstore.dto.request.ProductGetStatisticsRequest;
import com.netcracker.ncstore.dto.request.ProductUpdateRequest;
import com.netcracker.ncstore.dto.response.ProductCreateResponse;
import com.netcracker.ncstore.dto.response.ProductDeleteResponse;
import com.netcracker.ncstore.dto.response.ProductGetDetailedResponse;
import com.netcracker.ncstore.dto.response.ProductGetInfoResponse;
import com.netcracker.ncstore.dto.response.ProductGetStatisticsResponse;
import com.netcracker.ncstore.dto.response.ProductUpdateResponse;
import com.netcracker.ncstore.dto.response.ProductsGetPaginationResponse;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;

import java.util.List;

/**
 * Interface for all WEB services related to Product.
 */
public interface IProductWebService {
    /**
     * Returns response for get products with paginating request
     *
     * @param request filtering, sorting and pagination parameters
     * @return List<ProductsGetPaginationResponse
     */
    List<ProductsGetPaginationResponse> getPageOfFilteredAndSortedProducts(final ProductGetPaginationRequest request);

    /**
     * Creates new product and returns response
     *
     * @param request request for creating product
     * @return ProductCreateResponse
     * @throws GeneralBadRequestException when creation was not successful
     */
    ProductCreateResponse createNewProductInStore(final ProductCreateRequest request)
            throws GeneralBadRequestException;

    /**
     * Updates product and returns response
     *
     * @param request request for updating product
     * @return ProductUpdateResponse
     * @throws GeneralBadRequestException when creation was not successful
     */
    ProductUpdateResponse updateExistingProduct(final ProductUpdateRequest request)
            throws GeneralPermissionDeniedException, GeneralNotFoundException, GeneralBadRequestException;

    /**
     * Deletes product and returns response
     *
     * @param request request for deleting product
     * @return ProductDeleteResponse
     * @throws GeneralBadRequestException when deletion was not successful
     */
    ProductDeleteResponse deleteProductFromStore(final ProductDeleteRequest request)
            throws GeneralPermissionDeniedException, GeneralNotFoundException;

    /**
     * Returns response containing basic product info. For regular customers.
     *
     * @param request request specifying the product
     * @return ProductGetInfoResponse
     */
    ProductGetInfoResponse getProductInfo(final ProductGetInfoRequest request);

    /**
     * Returns response containing detailed product info. For suppliers.
     *
     * @param request request specifying the product
     * @return ProductGetDetailedResponse
     */
    ProductGetDetailedResponse getDetailedProductInfo(final ProductGetDetailedRequest request);

    /**
     * Returns response containing statistics of product. For suppliers.
     *
     * @param request request specifying the product
     * @return ProductGetStatisticsResponse
     */
    ProductGetStatisticsResponse getStatisticsForProduct(final ProductGetStatisticsRequest request);
}
