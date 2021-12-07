package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.*;
import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.data.CategoryDTO;
import com.netcracker.ncstore.dto.data.DiscountDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.dto.request.ProductCreateRequest;
import com.netcracker.ncstore.dto.request.ProductGetRequest;
import com.netcracker.ncstore.dto.request.ProductUpdateRequest;
import com.netcracker.ncstore.dto.response.*;
import com.netcracker.ncstore.exception.DiscountServiceNotFoundException;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.service.category.ICategoryService;
import com.netcracker.ncstore.service.discount.IDiscountsService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.product.IProductsService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.util.converter.ProductRequestConverter;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import com.netcracker.ncstore.util.enumeration.ESortRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
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
    private final IUserService userService;
    private final IDiscountsService discountsService;

    /**
     * Constructor
     * <p>
     * TODO: In the future, any services should be the arguments of constructor
     */
    public ProductController(final IProductsService productsService,
                             final IPricesService pricesService,
                             final ICategoryService categoryService,
                             final IUserService userService,
                             final IDiscountsService discountsService) {
        this.log = LoggerFactory.getLogger(ProductController.class);
        this.productsService = productsService;
        this.pricesService = pricesService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.discountsService = discountsService;
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/getProducts
    @RequestMapping(value = "/products", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<ProductsGetPaginationResponse>> getProductsWithPagination(
            @RequestParam(defaultValue = "", required = false) final String categoryIds,
            @RequestParam(defaultValue = "", required = false) final String searchText,
            @RequestParam(defaultValue = "default", required = false) final String sort,
            @RequestParam(defaultValue = "asc", required = false) final String sortOrder,
            @RequestParam(required = false) final UUID supplierId,
            @RequestParam final int page,
            @RequestParam final int size,
            final Locale locale) {

        log.info("REQUEST: to get products by search text='" + searchText + "'; for supplierId='" + supplierId + "'; order by " + sort + " " + sortOrder + "; on: " + page + " page, with " + size + " size");

        ESortRule sortEnum = ProductRequestConverter.convertSortRuleStringToEnum(sort);
        ESortOrder sortOrderEnum = ProductRequestConverter.convertSortOrderStringToEnum(sortOrder);

        List<UUID> categories = ProductRequestConverter.convertCategoriesStringToList(categoryIds);

        ProductGetRequest productGetRequest =
                new ProductGetRequest(categories, searchText, page, size, locale, sortEnum, sortOrderEnum, supplierId);

        List<ProductsGetPaginationResponse> response = productsService.getPageOfProductsUsingFilterAndSortParameters(productGetRequest);

        log.info("RESPONSE: to get products by search text:" + searchText + " on: " + page + " page, with " + size + " size");
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/createProduct
    @RequestMapping(value = "/products", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<CreateProductResponse> createProduct(@RequestBody final ProductCreateRequest request,
                                                               final Principal principal) {
        log.info("REQUEST: to create product with name " + request.getProductName() + " for user with UUID: " + userService.loadUserByEmail(principal.getName()).getId());

        ProductCreateDTO productData = new ProductCreateDTO(
                request.getProductName(),
                request.getProductDescription(),
                principal,
                EProductStatus.IN_STOCK,
                request.getNormalPrices(),
                request.getDiscountPrices(),
                request.getParentProductId(),
                request.getCategoriesNames()
        );

        ProductDTO productDTO = productsService.createNewProductInStore(productData);

        List<PriceRegionDTO> priceRegionDTOS = new ArrayList<>();
        List<DiscountPriceRegionDTO> discountPriceRegionDTOS = new ArrayList<>();

        for (ProductPriceDTO p : pricesService.getPricesForProduct(productDTO.getId())) {
            priceRegionDTOS.add(new PriceRegionDTO(p.getPrice(), p.getLocale()));

            try {
                DiscountDTO discountDTO = discountsService.getDiscountForPrice(p.getId());
                discountPriceRegionDTOS.add(new DiscountPriceRegionDTO(
                        discountDTO.getDiscountPrice(),
                        p.getLocale(),
                        discountDTO.getStartUtcTime(),
                        discountDTO.getEndUtcTime()));

            } catch (DiscountServiceNotFoundException ignored) {
            }
        }

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
                discountPriceRegionDTOS,
                productDTO.getParentProductId(),
                categoryNames
        );

        log.info("RESPONSE: to create product with name " + request.getProductName() + " for user with UUID: " + userService.loadUserByEmail(principal.getName()).getId());
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.3#/Product/getProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ProductGetResponse> getProduct(@PathVariable final UUID productId, final Locale locale) {
        log.info("REQUEST: to get product data by id: " + productId);
        ProductGetResponse response = productsService.getProductResponse(new ProductLocaleDTO(productId, locale));
        log.info("RESPONSE: to get product data by id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @RequestMapping(value = "/products/{productId}/detailed", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<GetProductResponse> getProductDetailed(@PathVariable final UUID productId) {
        log.info("REQUEST: to get product detailed data by id: " + productId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GetProductResponse response = productsService.getProductDetailed(
                new ProductIdAuthDTO(
                        productId,
                        new UserEmailAndRolesDTO(authentication)
                )
        );

        log.info("RESPONSE: to get product detailed data by id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.3#/Product/updateProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable final UUID productId,
                                                               @RequestBody final ProductUpdateRequest request) {
        log.info("REQUEST: to update product with id: " + productId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UpdateProductResponse response = productsService.updateProduct(
                new ProductIdUpdateRequestAuthDTO(
                        productId,
                        request,
                        new UserEmailAndRolesDTO(authentication)
                )
        );

        log.info("RESPONSE: to update product with id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.3#/Product/deleteProduct
    @RequestMapping(value = "/products/{productId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity<DeleteProductResponse> deleteProduct(@PathVariable final UUID productId) {
        log.info("REQUEST: to delete product with id: " + productId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DeleteProductResponse response = productsService.deleteProduct(
                new ProductIdAuthDTO(
                        productId,
                        new UserEmailAndRolesDTO(authentication)
                )
        );
        log.info("RESPONSE: to delete product with id: " + productId);
        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    // https://app.swaggerhub.com/apis/netcrstore/ncstore/1.0.1#/Product/buyProduct

    @RequestMapping(value = "/products/{productId}/buy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> buyProduct(@PathVariable final UUID productId) {
        return null;
    }
}
