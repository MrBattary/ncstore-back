package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.ActualProductPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.exception.CategoryServiceNotFoundException;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.exception.ProductServiceCreationValidationException;
import com.netcracker.ncstore.exception.ProductServiceCreationException;
import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.repository.projections.ProductWithPriceInfo;
import com.netcracker.ncstore.service.category.ICategoryService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import com.netcracker.ncstore.util.validator.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Service responsible for any logic related to Product entity.
 * Is a default implementation of IPricesService.
 */
@Service
public class ProductsService implements IProductsService {
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    private final Logger log;
    private final ProductRepository productRepository;
    private final IPricesService pricesService;
    private final IUserService userService;
    private final ICategoryService categoryService;

    public ProductsService(final ProductRepository productRepository,
                           final IPricesService pricesService,
                           final IUserService userService,
                           final ICategoryService categoryService) {
        this.log = LoggerFactory.getLogger(ProductsService.class);
        this.productRepository = productRepository;
        this.pricesService = pricesService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public ProductsGetResponse getPageOfProductsUsingFilterAndSortParameters(final ProductsGetRequest productsGetRequest) {
        Pageable productsPageRequest;
        Page<ProductWithPriceInfo> productsPage;

        Sort.Direction direction = productsGetRequest.getSortOrderString().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;


        switch (productsGetRequest.getSortString()) {
            default:
            case "default":
            case "popular"://TODO separate and make when rating will be
            case "rating"://TODO separate and make when rating will be
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(direction, "name"));
                break;
            case "price":
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(direction, "pp.price - coalesce(d.discountPrice,0)"));
                break;
            case "new":
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(direction, "creationUtcTime"));
                break;
            case "discount":
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(Sort.Direction.ASC, "coalesce(d.discountPrice,pp.price)/pp.price"));
                break;
        }

        if (productsGetRequest.getCategoriesIds().size() != 0) {
            productsPage = productRepository.findProductsByLikeNameAndCategoriesAndLocale(
                    productsGetRequest.getSearchText(),
                    productsGetRequest.getLocale(),
                    productsGetRequest.getCategoriesIds(),
                    productsPageRequest);
        } else {
            productsPage = productRepository.findProductByLikeNameAndLocale(
                    productsGetRequest.getSearchText(),
                    productsGetRequest.getLocale(),
                    Locale.forLanguageTag(defaultLocaleCode),
                    productsPageRequest);
        }

        List<ActualProductPriceWithCurrencySymbolDTO> productPriceInRegionDTOS =
                new ArrayList<>();

        for (ProductWithPriceInfo productInfo : productsPage.getContent()) {
            ProductWithPriceInfo.ProductPriceInfo firstPrice = productInfo.getProductPrices().get(0);

            ActualProductPriceWithCurrencySymbolDTO priceInRegion = new ActualProductPriceWithCurrencySymbolDTO(
                    productInfo.getId(),
                    productInfo.getName(),
                    firstPrice.getPrice(),
                    firstPrice.getDiscount() == null ? null : firstPrice.getDiscount().getDiscountPrice(),
                    LocaleToCurrencyConverter.getCurrencySymbolByLocale(firstPrice.getLocale())
            );

            productPriceInRegionDTOS.add(priceInRegion);
        }

        return new ProductsGetResponse(productPriceInRegionDTOS);
    }

    @Override
    @Transactional
    public ProductDTO createNewProductInStore(final ProductCreateDTO productData) throws ProductServiceCreationException {
        User creator = userService.loadUserEntityByPrincipal(productData.getPrincipal());
        log.info("Creation of new product for user with UUID " + creator.getId() + " begins");

        try {
            validateProductCreationData(productData);

            Product parentProduct = null;
            if (productData.getParentProductUUID() != null) {
                parentProduct = productRepository.getById(productData.getParentProductUUID());
            }

            List<Category> categories =
                    productData.getCategoriesNames().
                            stream().
                            map(categoryService::getCategoryEntityByName).
                            collect(Collectors.toList());

            Product product = productRepository.save(new Product(
                    null,
                    productData.getName(),
                    productData.getDescription(),
                    Instant.now(),
                    parentProduct,
                    creator,
                    productData.getStatus(),
                    null,
                    categories));


            productData.getPrices().
                    stream().
                    map(e -> new ProductPriceCreateDTO(e.getPrice(), e.getRegion(), product)).
                    forEach(pricesService::createProductPrice);

            log.info("New Product with UUID " + product.getId() + " for user with UUID " + creator.getId() + " created successfully");
            return new ProductDTO(product);

        } catch (ProductServiceCreationValidationException |
                CategoryServiceNotFoundException |
                PricesServiceValidationException e) {

            log.error(e.getMessage());
            throw new ProductServiceCreationException("Unable to create new product for user with UUID " + creator.getId(), e);
        }
    }

    private void validateProductCreationData(final ProductCreateDTO productCreateDTO) {
        User creator = userService.loadUserEntityByPrincipal(productCreateDTO.getPrincipal());

        boolean isSupplier = creator.getRoles().stream().anyMatch(e -> e.getRoleName().equals(ERoleName.SUPPLIER));
        if (!isSupplier) {
            throw new ProductServiceCreationValidationException("User with UUID " + creator.getId() + " tried to create product while not having SUPPLIER role");
        }


        if (!ProductValidator.checkCategoriesNamesList(productCreateDTO.getCategoriesNames())) {
            throw new ProductServiceCreationValidationException("Categories list not provided or empty");
        }

        if (productCreateDTO.getParentProductUUID() != null) {
            Product parentProduct = productRepository.findById(productCreateDTO.getParentProductUUID()).orElse(null);
            if (parentProduct == null) {
                throw new ProductServiceCreationValidationException("Product with UUID " + productCreateDTO.getParentProductUUID() + " not found, but was requested as parent product for new product.");
            }
        }


        if (productCreateDTO.getPrices() == null) {
            throw new ProductServiceCreationValidationException("No price for default Locale with tag " + defaultLocaleCode + " was provided. Could not create product.");
        }

        boolean hasDefaultLocale = ProductValidator.hasProvidedLocale(
                productCreateDTO.getPrices().stream()
                        .map(PriceRegionDTO::getRegion)
                        .collect(Collectors.toList()), defaultLocaleCode);

        if (!hasDefaultLocale) {
            throw new ProductServiceCreationValidationException("No price for default Locale with tag " + defaultLocaleCode + " was provided. Could not create product.");
        }

        if (!ProductValidator.isNameValid(productCreateDTO.getName())) {
            throw new ProductServiceCreationValidationException("Product name not provided or it length is not between (3;255)");
        }

        if (!ProductValidator.isDescriptionValid(productCreateDTO.getDescription())) {
            throw new ProductServiceCreationValidationException("Product description not provided or is shorter than 50 symbols");
        }
    }
}
