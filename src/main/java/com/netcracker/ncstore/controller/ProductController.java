package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.service.product.ProductsService;
import com.netcracker.ncstore.util.converter.ProductRequestConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Product controller is responsible for any actions with products
 */
@RestController
public class ProductController {
    private final Logger log;
    private final ProductsService productsService;

    /**
     * Constructor
     * <p>
     * TODO: In the future, any services should be the arguments of constructor
     */
    public ProductController(final ProductsService productsService) {
        this.log = LoggerFactory.getLogger(ProductController.class);
        this.productsService = productsService;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/getProducts
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ProductPriceInRegionDTO>> getProductsWithPagination(
            @RequestParam(defaultValue = "", required = false) final String categoriesIds,
            @RequestParam(defaultValue = "", required = false) final String searchText,
            @RequestParam(defaultValue = "default", required = false) final String sort,
            @RequestParam(defaultValue = "asc") final String sortOrder,
            @RequestParam(required = false) final UUID supplierId,
            @RequestParam final int page,
            @RequestParam final int size,
            final Locale locale) {

        log.info("REQUEST: to get products by search text:" + searchText + " on: " + page + " page, with " + size + " size");

        List<UUID> categories = ProductRequestConverter.convertCategoriesStringToList(categoriesIds);

        ProductsGetRequest productsGetRequest =
                new ProductsGetRequest(categories, searchText, page, size, locale, sort, sortOrder, supplierId);

        ProductsGetResponse response = productsService.getPageOfProductsUsingFilterAndSortParameters(productsGetRequest);

        log.info("RESPONSE: to get products by search text:" + searchText + " on: " + page + " page, with " + size + " size");
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response.getProductsWithPrices());
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/createProduct
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createProduct() {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/getProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> getProduct(@PathVariable final String productId) {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/updateProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateProduct(@PathVariable final String productId) {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/deleteProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@PathVariable final String productId) {
        return null;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/buyProduct

    @RequestMapping(value = "/products/{productId}/buy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> buyProduct(@PathVariable final String productId) {
        return null;
    }
}
