package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.ActualProductPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.data.CategoryDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.request.CreateProductRequest;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.CreateProductResponse;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.service.category.ICategoryService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.product.IProductsService;
import com.netcracker.ncstore.util.converter.ProductRequestConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
public class ProductController {
    private final Logger log;
    private final IProductsService productsService;
    private final IPricesService pricesService;
    private final ICategoryService categoryService;

    /**
     * Constructor
     * <p>
     * TODO: In the future, any services should be the arguments of constructor
     */
    public ProductController(final IProductsService productsService,
                             final IPricesService pricesService,
                             final ICategoryService categoryService) {
        this.log = LoggerFactory.getLogger(ProductController.class);
        this.productsService = productsService;
        this.pricesService = pricesService;
        this.categoryService = categoryService;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/getProducts
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ActualProductPriceWithCurrencySymbolDTO>> getProductsWithPagination(
            @RequestParam(defaultValue = "") final String categoriesIds,
            @RequestParam final String searchText,
            @RequestParam final int page,
            @RequestParam final int size,
            final Locale locale) {

        log.info("REQUEST: to get products by search text:" + searchText + " on: " + page + " page, with " + size + " size");

        List<UUID> categories = ProductRequestConverter.convertCategoriesStringToList(categoriesIds);

        ProductsGetRequest productsGetRequest =
                new ProductsGetRequest(categories, searchText, page, size, locale);

        ProductsGetResponse response = productsService.getPageOfProductsByNameAndCategories(productsGetRequest);

        log.info("RESPONSE: to get products by search text:" + searchText + " on: " + page + " page, with " + size + " size");
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response.getProductsWithPrices());
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/createProduct
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> createProduct(@RequestBody CreateProductRequest request, Principal principal) {
        ProductCreateDTO productData = new ProductCreateDTO(
                request.getProductName(),
                request.getProductDescription(),
                principal,
                EProductStatus.IN_STOCK,
                request.getRegionalPrices(),
                request.getParentProductId(),
                request.getCategoriesNames()
        );

        ProductDTO productDTO = productsService.createNewProductInStore(productData);

        List<PriceRegionDTO> priceRegionDTOS = pricesService.
                getPricesForProduct(productDTO.getId())
                .stream()
                .map(e->new PriceRegionDTO(e.getPrice(),e.getLocale()))
                .collect(Collectors.toList());

        List<String> categoryNames = categoryService.
                getCategoriesForProduct(productDTO.getId()).
                stream().map(CategoryDTO::getName).
                collect(Collectors.toList());

        CreateProductResponse response = new CreateProductResponse(
                productDTO.getId(),
                productDTO.getSupplierId(),
                productDTO.getName(),
                productDTO.getDescription(),
                priceRegionDTOS,
                productDTO.getParentProductId(),
                categoryNames
        );

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
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
