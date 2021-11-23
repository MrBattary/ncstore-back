package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.exception.CategoryServiceNotFoundException;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.exception.ProductServiceCreationException;
import com.netcracker.ncstore.exception.ProductServiceCreationValidationException;
import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.repository.projections.ProductWithPriceInfo;
import com.netcracker.ncstore.service.category.ICategoryService;
import com.netcracker.ncstore.service.discount.IDiscountsService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import com.netcracker.ncstore.util.validator.PriceValidator;
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
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
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
    private final IDiscountsService discountsService;

    public ProductsService(final ProductRepository productRepository,
                           final IPricesService pricesService,
                           final IUserService userService,
                           final ICategoryService categoryService,
                           final IDiscountsService discountsService) {
        this.log = LoggerFactory.getLogger(ProductsService.class);
        this.productRepository = productRepository;
        this.pricesService = pricesService;
        this.userService = userService;
        this.categoryService = categoryService;
        this.discountsService = discountsService;
    }

    @Override
    public List<ProductsGetResponse> getPageOfProductsUsingFilterAndSortParameters(final ProductsGetRequest productsGetRequest) {
        Pageable productsPageRequest;
        Page<ProductWithPriceInfo> productsPage;

        Sort.Direction direction = productsGetRequest.getSortOrder().equals(ESortOrder.ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;


        switch (productsGetRequest.getSort()) {
            default:
            case DEFAULT:
            case POPULAR://TODO separate and make when rating will be
            case RATING://TODO separate and make when rating will be
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(direction, "name"));
                break;
            case PRICE:
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(direction, "pp.price - coalesce(d.discountPrice,0)"));
                break;
            case DATE:
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(direction, "creationUtcTime"));
                break;
            case DISCOUNT:
                productsPageRequest = PageRequest.of(
                        productsGetRequest.getPage(),
                        productsGetRequest.getSize(),
                        JpaSort.unsafe(Sort.Direction.ASC, "coalesce(d.discountPrice,pp.price)/pp.price"));
                break;
        }

        if (CollectionUtils.isEmpty(productsGetRequest.getCategoriesIds())) {
            if (productsGetRequest.getSupplierId() != null) {
                productsPage = productRepository.findProductByUserIdAndByLikeNameAndLocale(
                        productsGetRequest.getSupplierId(),
                        productsGetRequest.getSearchText(),
                        productsGetRequest.getLocale(),
                        Locale.forLanguageTag(defaultLocaleCode),
                        productsPageRequest);
            } else {
                productsPage = productRepository.findProductByLikeNameAndLocale(
                        productsGetRequest.getSearchText(),
                        productsGetRequest.getLocale(),
                        Locale.forLanguageTag(defaultLocaleCode),
                        productsPageRequest);
            }
        } else {
            if (productsGetRequest.getSupplierId() != null) {
                productsPage = productRepository.findProductsUserIdAndByLikeNameAndCategoriesAndLocale(
                        productsGetRequest.getSupplierId(),
                        productsGetRequest.getSearchText(),
                        productsGetRequest.getLocale(),
                        Locale.forLanguageTag(defaultLocaleCode),
                        productsGetRequest.getCategoriesIds(),
                        productsPageRequest);
            } else {
                productsPage = productRepository.findProductsByLikeNameAndCategoriesAndLocale(
                        productsGetRequest.getSearchText(),
                        productsGetRequest.getLocale(),
                        Locale.forLanguageTag(defaultLocaleCode),
                        productsGetRequest.getCategoriesIds(),
                        productsPageRequest);
            }
        }


        List<ProductsGetResponse> responsesList = new ArrayList<>();

        for (ProductWithPriceInfo productInfo : productsPage.getContent()) {
            ProductWithPriceInfo.ProductPriceInfo firstPrice = productInfo.getProductPrices().get(0);

            String supplierName =
                    userService.getCompanyData(productInfo.getUserId()) == null
                            ?
                            userService.getPersonData(productInfo.getUserId()).getFirstName() + " " + userService.getPersonData(productInfo.getUserId()).getLastName()
                            :
                            userService.getCompanyData(productInfo.getUserId()).getCompanyName();

            ProductsGetResponse response = new ProductsGetResponse(
                    productInfo.getId(),
                    productInfo.getName(),
                    productInfo.getUserId(),
                    supplierName,
                    firstPrice.getPrice(),
                    PriceValidator.getActualDiscountPrice(firstPrice.getDiscount()),
                    LocaleToCurrencyConverter.getCurrencySymbolByLocale(firstPrice.getLocale()));

            responsesList.add(response);
        }

        return responsesList;
    }

    @Override
    @Transactional
    public ProductDTO createNewProductInStore(final ProductCreateDTO productData) throws ProductServiceCreationException {
        User creator = userService.loadUserEntityByPrincipal(productData.getPrincipal());
        log.info("Creation of new product for user with UUID " + creator.getId() + " begins");

        try {
            validateProductData(productData);

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


            List<ProductPriceDTO> createdPricesDTOs = productData.getPrices().
                    stream().
                    map(e -> new ProductPriceCreateDTO(e.getPrice(), e.getRegion(), product.getId())).
                    map(pricesService::createProductPrice).
                    collect(Collectors.toList());

            Map<Locale, UUID> localeProductPriceUUIDMap =
                    createdPricesDTOs.
                            stream().
                            collect(Collectors.toMap(ProductPriceDTO::getLocale, ProductPriceDTO::getId));

            if (productData.getDiscountPrices() != null) {
                productData.getDiscountPrices().
                        stream().
                        map(e -> new DiscountCreateDTO(
                                e.getPrice(),
                                e.getStartUtcTime(),
                                e.getEndUtcTime(),
                                localeProductPriceUUIDMap.get(e.getRegion()))).
                        forEach(discountsService::createNewDiscountForPrice);
            }

            log.info("New Product with UUID " + product.getId() + " for user with UUID " + creator.getId() + " created successfully");
            return new ProductDTO(product);

        } catch (ProductServiceCreationValidationException |
                CategoryServiceNotFoundException |
                PricesServiceValidationException e) {

            log.error(e.getMessage());
            throw new ProductServiceCreationException("Unable to create new product for user with UUID " + creator.getId(), e);
        }
    }

    private void validateProductData(final ProductCreateDTO productCreateDTO) {
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
            throw new ProductServiceCreationValidationException("No prices was provided. Could not create product.");
        }

        boolean hasDefaultLocale = ProductValidator.hasProvidedLocale(
                productCreateDTO.getPrices().stream()
                        .map(PriceRegionDTO::getRegion)
                        .collect(Collectors.toList()), defaultLocaleCode);

        if (!hasDefaultLocale) {
            throw new ProductServiceCreationValidationException("No price for default Locale with tag " + defaultLocaleCode + " was provided. Could not create product.");
        }

        if (!PriceValidator.isPriceDuplicates(productCreateDTO.getPrices())) {
            throw new ProductServiceCreationValidationException("Price duplicates (with same locale) found.");
        }

        if (productCreateDTO.getDiscountPrices() != null) {
            if (!PriceValidator.isDiscountsValid(productCreateDTO.getPrices(), productCreateDTO.getDiscountPrices())) {
                throw new ProductServiceCreationValidationException("Discount prices are invalid. Each discount price must have normal price in same region.");
            }
        }

        if (!ProductValidator.isNameValid(productCreateDTO.getName())) {
            throw new ProductServiceCreationValidationException("Product name not provided or it length is not between (3;255)");
        }

        if (!ProductValidator.isDescriptionValid(productCreateDTO.getDescription())) {
            throw new ProductServiceCreationValidationException("Product description not provided or is shorter than 50 symbols");
        }
    }
}
