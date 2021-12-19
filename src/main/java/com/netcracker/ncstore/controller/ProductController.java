package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.ProductCreateBody;
import com.netcracker.ncstore.dto.body.ProductUpdateBody;
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
import com.netcracker.ncstore.service.product.interfaces.IProductWebService;
import com.netcracker.ncstore.util.converter.ProductRequestConverter;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import com.netcracker.ncstore.util.enumeration.ESortRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Product controller is responsible for any actions with products
 */
@RestController
@Slf4j
public class ProductController {
    private final IProductWebService productWebService;

    public ProductController(IProductWebService productWebService) {
        this.productWebService = productWebService;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/getProducts
    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductsGetPaginationResponse>> getProductsWithPagination(
            @RequestParam(defaultValue = "", required = false) final String categoryNames,
            @RequestParam(defaultValue = "", required = false) final String searchText,
            @RequestParam(defaultValue = "default", required = false) final String sort,
            @RequestParam(defaultValue = "asc", required = false) final String sortOrder,
            @RequestParam(required = false) final UUID supplierId,
            @RequestParam final int page,
            @RequestParam final int size,
            final Locale locale) {

        List<String> categories = ProductRequestConverter.convertCategoriesStringToList(categoryNames);
        log.info("REQUEST: to get products with categories: '" +
                categories.stream().map(Object::toString).collect(Collectors.joining(" ")) +
                "', by search text='" + searchText + "', for supplierId='" + supplierId + "', order by " + sort + " " + sortOrder + ", on: " + page + " page, with " + size + " size");


        ESortRule sortEnum = ProductRequestConverter.convertSortRuleStringToEnum(sort);
        ESortOrder sortOrderEnum = ProductRequestConverter.convertSortOrderStringToEnum(sortOrder);


        ProductGetPaginationRequest productGetPaginationRequest =
                new ProductGetPaginationRequest(categories, searchText, page, size, locale, sortEnum, sortOrderEnum, supplierId);

        List<ProductsGetPaginationResponse> response = productWebService.getPageOfFilteredAndSortedProducts(productGetPaginationRequest);

        log.info("RESPONSE: to get products by search text:" + searchText + " on: " + page + " page, with " + size + " size");
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @PostMapping(value = "/products")
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody final ProductCreateBody body,
                                                               final Principal principal) {
        log.info("REQUEST: to create product for user with email " + principal.getName());

        ProductCreateRequest request = new ProductCreateRequest(
                principal.getName(),
                body.getProductName(),
                body.getProductDescription(),
                body.getNormalPrices(),
                body.getDiscountPrices(),
                body.getParentProductId(),
                body.getCategoriesNames()
        );

        ProductCreateResponse response = productWebService.createNewProductInStore(request);

        log.info("RESPONSE: to create product with name " + request.getProductName() + " for user with email: " + principal.getName());
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @GetMapping(value = "/products/{productId}")
    public ResponseEntity<ProductGetInfoResponse> getProduct(@PathVariable final UUID productId,
                                                             final Locale locale) {
        log.info("REQUEST: to get product data by id: " + productId);

        ProductGetInfoRequest request = new ProductGetInfoRequest(
                productId,
                locale
        );

        ProductGetInfoResponse response = productWebService.getProductInfo(request);

        log.info("RESPONSE: to get product data by id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @GetMapping(value = "/products/{productId}/detailed")
    public ResponseEntity<ProductGetDetailedResponse> getProductDetailed(@PathVariable final UUID productId,
                                                                         final Principal principal) {
        log.info("REQUEST: to get detailed product data by id: " + productId);

        ProductGetDetailedRequest request = new ProductGetDetailedRequest(
                productId,
                principal.getName()
        );

        ProductGetDetailedResponse response = productWebService.getDetailedProductInfo(request);

        log.info("RESPONSE: to get detailed product data by id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @PutMapping(value = "/products/{productId}")
    public ResponseEntity<ProductUpdateResponse> updateProduct(@PathVariable final UUID productId,
                                                               @RequestBody final ProductUpdateBody body,
                                                               final Principal principal) {
        log.info("REQUEST: to update product with id: " + productId);

        ProductUpdateRequest request = new ProductUpdateRequest(
                productId,
                principal.getName(),
                body.getProductName(),
                body.getProductDescription(),
                body.getNormalPrices(),
                body.getDiscountPrices(),
                body.getParentProductId(),
                body.getCategoriesNames()
        );

        ProductUpdateResponse response = productWebService.updateExistingProduct(request);

        log.info("RESPONSE: to update product with id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @DeleteMapping(value = "/products/{productId}")
    public ResponseEntity<ProductDeleteResponse> deleteProduct(@PathVariable final UUID productId,
                                                               final Principal principal) {
        log.info("REQUEST: to delete product with id: " + productId);

        ProductDeleteRequest request = new ProductDeleteRequest(
                productId,
                principal.getName()
        );

        ProductDeleteResponse response = productWebService.deleteProductFromStore(request);

        log.info("RESPONSE: to delete product with id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }
}
