package com.netcracker.ncstore.service.product.interfaces;

import com.netcracker.ncstore.dto.request.ProductCreateRequest;
import com.netcracker.ncstore.dto.request.ProductDeleteRequest;
import com.netcracker.ncstore.dto.request.ProductGetDetailedRequest;
import com.netcracker.ncstore.dto.request.ProductGetInfoRequest;
import com.netcracker.ncstore.dto.request.ProductGetPaginationRequest;
import com.netcracker.ncstore.dto.request.ProductUpdateRequest;
import com.netcracker.ncstore.dto.response.ProductCreateResponse;
import com.netcracker.ncstore.dto.response.ProductDeleteResponse;
import com.netcracker.ncstore.dto.response.ProductGetDetailedResponse;
import com.netcracker.ncstore.dto.response.ProductGetInfoResponse;
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
    List<ProductsGetPaginationResponse> getPageOfFilteredAndSortedProducts(final ProductGetPaginationRequest request);

    ProductCreateResponse createNewProductInStore(final ProductCreateRequest request)
            throws GeneralBadRequestException;

    ProductUpdateResponse updateExistingProduct(final ProductUpdateRequest request)
            throws GeneralPermissionDeniedException, GeneralNotFoundException, GeneralBadRequestException;

    ProductDeleteResponse deleteProductFromStore(final ProductDeleteRequest request)
            throws GeneralPermissionDeniedException, GeneralNotFoundException;

    ProductGetInfoResponse getProductInfo(final ProductGetInfoRequest request);

    ProductGetDetailedResponse getDetailedProductInfo(final ProductGetDetailedRequest request);
}
