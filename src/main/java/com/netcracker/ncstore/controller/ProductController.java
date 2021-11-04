package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.ProductsGetRequestDTO;
import com.netcracker.ncstore.dto.ProductsGetResponseDTO;
import com.netcracker.ncstore.exception.RequestParametersInvalidException;
import com.netcracker.ncstore.service.product.ProductsService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public ResponseEntity<ProductsGetResponseDTO> getProductsWithPagination(@RequestParam(defaultValue = "") final String categoryId,
                                                                            @RequestParam final String searchText,
                                                                            @RequestParam final int page,
                                                                            @RequestParam final int size,
                                                                            final Locale locale) {
        List<UUID> categories;

        if (!categoryId.equals("")) {
            try {
                categories = Arrays.stream(categoryId.split("\\|")).
                        map(UUID::fromString).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new RequestParametersInvalidException();
            }
        } else {
            categories = new ArrayList<>();
        }

        ProductsGetRequestDTO productsGetRequestDTO =
                new ProductsGetRequestDTO(categories, searchText, page, size, locale);

        ProductsGetResponseDTO responseObject =
                productsService.getPageOfProductsByNameAndCategories(productsGetRequestDTO);

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(responseObject);
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
