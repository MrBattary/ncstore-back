package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.*;
import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.create.ProductCreateDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.data.ProductDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.request.UpdateProductRequest;
import com.netcracker.ncstore.dto.response.DeleteProductResponse;
import com.netcracker.ncstore.dto.response.GetProductResponse;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.dto.response.UpdateProductResponse;
import com.netcracker.ncstore.exception.*;
import com.netcracker.ncstore.model.*;
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

import java.security.Principal;
import java.time.Instant;
import java.util.*;
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
        User creator = userService.loadUserEntityByEmail(productData.getPrincipal().getName());
        log.info("Creation of new product for user with UUID " + creator.getId() + " begins");

        try {
            validateProductName(productData.getName());
            validateProductDescription(productData.getDescription());
            validateSupplierRoleByPrincipal(productData.getPrincipal());
            validateCategoriesNamesList(productData.getCategoriesNames());
            validateParentProductId(productData.getParentProductUUID());
            validateProductPrices(productData.getPrices(), productData.getDiscountPrices());

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

        } catch (ProductServiceValidationException |
                CategoryServiceNotFoundException |
                PricesServiceValidationException e) {

            log.error(e.getMessage());
            throw new ProductServiceCreationException("Unable to create new product for user with UUID " + creator.getId(), e);
        }
    }

    @Override
    public boolean doesProductExist(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public GetProductResponse getProduct(final ProductIdLocaleDTO productIdLocaleDTO)
            throws ProductServiceNotFoundException {
        try {
            Product productFromRepository = findProductById(productIdLocaleDTO.getProductId());

            UUID idOfSupplierOfProductFromRepository = productFromRepository.getSupplier().getId();
            String supplierName = getSupplierNameByUserId(idOfSupplierOfProductFromRepository);

            ProductPrice regionProductPrice = null;
            ProductPrice defaultProductPrice = new ProductPrice();
            for (ProductPrice productPriceOfProductFromRepository : productFromRepository.getProductPrices()) {
                if (productPriceOfProductFromRepository.getLocale() == productIdLocaleDTO.getLocale()) {
                    regionProductPrice = productPriceOfProductFromRepository;
                    break;
                }
                if (Objects.equals(productPriceOfProductFromRepository.getLocale().toLanguageTag(), defaultLocaleCode)) {
                    defaultProductPrice = productPriceOfProductFromRepository;
                }
            }
            ProductPrice foundProductPriceOfProductFromRepository =
                    regionProductPrice == null
                            ?
                            defaultProductPrice
                            :
                            regionProductPrice;
            List<ProductPrice> requiredProductPriceList =
                    new ArrayList<>(Collections.singletonList(foundProductPriceOfProductFromRepository));

            List<PriceRegionDTO> normalPrices =
                    pricesService.getListOfPriceRegionDtoByListOfPrices(requiredProductPriceList);
            List<DiscountPriceRegionDTO> discountPrices =
                    pricesService.getListOfDiscountPriceRegionDtoByListOfPrices(requiredProductPriceList);

            return new GetProductResponse(
                    productFromRepository.getId(),
                    productFromRepository.getName(),
                    idOfSupplierOfProductFromRepository,
                    supplierName,
                    productFromRepository.getDescription(),
                    normalPrices,
                    discountPrices,
                    productFromRepository.getParentProduct() != null ? productFromRepository.getParentProduct().getId() : null,
                    productFromRepository.getCategories().stream().map(Category::getName).collect(Collectors.toList())
            );
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ProductServiceNotFoundException("Unable to get product with UUID" + productIdLocaleDTO.getProductId(), e);
        }
    }

    @Override
    public GetProductResponse getProductDetailed(final ProductIdAuthDTO productIdAuthDTO)
            throws ProductServiceNotFoundException, ProductServiceNotAllowedException {
        try {
            log.info("The receipt of detailed information about product has begun for user with email: "
                    + productIdAuthDTO.getUserEmailAndRolesDTO().getEmail());
            Product productFromRepository = findProductById(productIdAuthDTO.getProductId());

            validateSupplierByEmail(
                    productFromRepository.getSupplier().getEmail(),
                    productIdAuthDTO.getUserEmailAndRolesDTO()
            );

            UUID idOfSupplierOfProductFromRepository = productFromRepository.getSupplier().getId();
            String supplierName = getSupplierNameByUserId(idOfSupplierOfProductFromRepository);

            List<PriceRegionDTO> normalPrices =
                    pricesService.getListOfPriceRegionDtoByListOfPrices(productFromRepository.getProductPrices());
            List<DiscountPriceRegionDTO> discountPrices =
                    pricesService.getListOfDiscountPriceRegionDtoByListOfPrices(productFromRepository.getProductPrices());

            log.info("The receipt of detailed information about product has ended for user with email: "
                    + productIdAuthDTO.getUserEmailAndRolesDTO().getEmail());
            return new GetProductResponse(
                    productFromRepository.getId(),
                    productFromRepository.getName(),
                    idOfSupplierOfProductFromRepository,
                    supplierName,
                    productFromRepository.getDescription(),
                    normalPrices,
                    discountPrices,
                    productFromRepository.getParentProduct() != null ? productFromRepository.getParentProduct().getId() : null,
                    productFromRepository.getCategories().stream().map(Category::getName).collect(Collectors.toList())
            );
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw new ProductServiceNotFoundException("Unable to get product with UUID" + productIdAuthDTO.getProductId(), e);
        }
    }

    @Override
    @Transactional
    public UpdateProductResponse updateProduct(ProductIdUpdateRequestAuthDTO productIdUpdateRequestAuthDTO)
            throws ProductServiceNotFoundException, ProductServiceNotAllowedException, ProductServiceValidationException {
        try {
            log.info("The update of the product information started by the request of the user with email: "
                    + productIdUpdateRequestAuthDTO.getUserEmailAndRolesDTO().getEmail());
            Product productFromRepository = findProductById(productIdUpdateRequestAuthDTO.getProductId());

            validateSupplierByEmail(
                    productFromRepository.getSupplier().getEmail(),
                    productIdUpdateRequestAuthDTO.getUserEmailAndRolesDTO()
            );

            UpdateProductRequest newProductData = productIdUpdateRequestAuthDTO.getRequest();

            validateProductName(newProductData.getProductName());
            validateProductDescription(newProductData.getProductDescription());
            validateParentProductId(newProductData.getParentProductId());
            validateCategoriesNamesList(newProductData.getCategoriesNames());
            validateProductPrices(
                    newProductData.getNormalPrices(),
                    newProductData.getDiscountPrices()
            );

            Product parentProduct = null;
            if (newProductData.getParentProductId() != null) {
                parentProduct = productRepository.getById(newProductData.getParentProductId());
            }

            List<Category> categories =
                    newProductData.getCategoriesNames().
                            stream().
                            map(categoryService::getCategoryEntityByName).
                            collect(Collectors.toList());

            productFromRepository.setName(newProductData.getProductName());
            productFromRepository.setDescription(newProductData.getProductDescription());
            productFromRepository.setParentProduct(parentProduct);
            productFromRepository.setCategories(categories);

            pricesService.deleteAllProvidedPrices(productFromRepository.getProductPrices());

            List<ProductPriceDTO> newPricesDTOs = newProductData.getNormalPrices().
                    stream().
                    map(e -> new ProductPriceCreateDTO(e.getPrice(), e.getRegion(), productFromRepository.getId())).
                    map(pricesService::createProductPrice).
                    collect(Collectors.toList());

            Map<Locale, UUID> localeProductPriceUUIDMap =
                    newPricesDTOs.
                            stream().
                            collect(Collectors.toMap(ProductPriceDTO::getLocale, ProductPriceDTO::getId));

            if (newProductData.getDiscountPrices() != null) {
                newProductData.getDiscountPrices().
                        stream().
                        map(e -> new DiscountCreateDTO(
                                e.getPrice(),
                                e.getStartUtcTime(),
                                e.getEndUtcTime(),
                                localeProductPriceUUIDMap.get(e.getRegion()))).
                        forEach(discountsService::createNewDiscountForPrice);
            }

            log.info("Product with UUID " + productFromRepository.getId() + " for user with email: "
                    + productIdUpdateRequestAuthDTO.getUserEmailAndRolesDTO().getEmail() + " updated successfully");

            return new UpdateProductResponse(newProductData.getProductId(),
                    newProductData.getProductName(),
                    newProductData.getProductDescription(),
                    newProductData.getNormalPrices(),
                    newProductData.getDiscountPrices(),
                    parentProduct != null ? parentProduct.getId() : null,
                    newProductData.getCategoriesNames()
            );
        } catch (ProductServiceValidationException |
                IllegalArgumentException |
                CategoryServiceNotFoundException |
                PricesServiceValidationException e) {

            log.error(e.getMessage());
            throw new ProductServiceCreationException("Unable to create new product for user with email: "
                    + productIdUpdateRequestAuthDTO.getUserEmailAndRolesDTO().getEmail(), e);
        }
    }

    @Override
    @Transactional
    public DeleteProductResponse deleteProduct(ProductIdAuthDTO productIdAuthDTO)
            throws ProductServiceNotFoundExpectedException, ProductServiceNotAllowedException {
        try {
            log.info("The deletion of the product started by the request of the user with email: "
                    + productIdAuthDTO.getUserEmailAndRolesDTO().getEmail());
            Product productFromRepository = findProductById(productIdAuthDTO.getProductId());

            validateSupplierByEmail(
                    productFromRepository.getSupplier().getEmail(),
                    productIdAuthDTO.getUserEmailAndRolesDTO()
            );

            List<PriceRegionDTO> normalPrices =
                    pricesService.getListOfPriceRegionDtoByListOfPrices(productFromRepository.getProductPrices());
            List<DiscountPriceRegionDTO> discountPrices =
                    pricesService.getListOfDiscountPriceRegionDtoByListOfPrices(productFromRepository.getProductPrices());

            DeleteProductResponse response = new DeleteProductResponse(
                    productFromRepository.getId(),
                    productFromRepository.getName(),
                    productFromRepository.getDescription(),
                    normalPrices,
                    discountPrices,
                    productFromRepository.getParentProduct() != null ? productFromRepository.getParentProduct().getId() : null,
                    productFromRepository.getCategories().stream().map(Category::getName).collect(Collectors.toList())
            );

            pricesService.deleteAllProvidedPrices(productFromRepository.getProductPrices());
            productRepository.deleteProductById(productFromRepository.getId());

            log.info("The deletion of the product completed for the request of the user with email: "
                    + productIdAuthDTO.getUserEmailAndRolesDTO().getEmail());
            return response;
        } catch (ProductServiceValidationException | ProductServiceNotFoundException | IllegalArgumentException e) {
            log.info(e.getMessage());
            throw new ProductServiceNotFoundExpectedException("Unable to delete product for user with email: "
                    + productIdAuthDTO.getUserEmailAndRolesDTO().getEmail(), e);
        }
    }

    private void validateSupplierByEmail(final String emailOfSupplierFromProduct, UserEmailAndRolesDTO userEmailAndRolesDTO)
            throws ProductServiceValidationException {
        if (!Objects.equals(emailOfSupplierFromProduct, userEmailAndRolesDTO.getEmail())) {
            throw new ProductServiceNotAllowedException("Unable to find requested product for supplier with email: "
                    + userEmailAndRolesDTO.getEmail());
        }
    }

    private void validateSupplierRoleByPrincipal(final Principal principal) throws ProductServiceValidationException {
        User creator = userService.loadUserEntityByEmail(principal.getName());

        boolean isSupplier = creator.getRoles().stream().anyMatch(e -> e.getRoleName().equals(ERoleName.SUPPLIER));
        if (!isSupplier) {
            throw new ProductServiceValidationException("User with UUID " + creator.getId()
                    + " tried to create product while not having SUPPLIER role");
        }
    }

    private void validateCategoriesNamesList(final List<String> categoriesNames) throws ProductServiceValidationException {
        if (!ProductValidator.checkCategoriesNamesList(categoriesNames)) {
            throw new ProductServiceValidationException("Categories list not provided or empty");
        }
    }

    private void validateParentProductId(final UUID uuid) throws ProductServiceValidationException {
        if (uuid != null) {
            Product parentProduct = productRepository.findById(uuid).orElse(null);
            if (parentProduct == null) {
                throw new ProductServiceValidationException("Product with UUID " + uuid
                        + " not found, but was requested as parent product for new product.");
            }
        }
    }

    private void validateProductName(final String productName) throws ProductServiceValidationException {
        if (!ProductValidator.isNameValid(productName)) {
            throw new ProductServiceValidationException("Product name not provided or it length is not between (3;255)");
        }
    }

    private void validateProductDescription(final String productDescription) throws ProductServiceValidationException {
        if (!ProductValidator.isDescriptionValid(productDescription)) {
            throw new ProductServiceValidationException("Product description not provided or is shorter than 50 symbols");
        }
    }

    private void validateProductPrices(final List<PriceRegionDTO> prices, final List<DiscountPriceRegionDTO> discountPrices)
            throws ProductServiceValidationException {
        if (prices == null) {
            throw new ProductServiceValidationException("No prices was provided. Could not create product.");
        }

        boolean hasDefaultLocale = ProductValidator.hasProvidedLocale(
                prices.stream()
                        .map(PriceRegionDTO::getRegion)
                        .collect(Collectors.toList()), defaultLocaleCode);

        if (!hasDefaultLocale) {
            throw new ProductServiceValidationException("No price for default Locale with tag " + defaultLocaleCode
                    + " was provided. Could not create product.");
        }

        if (!PriceValidator.isPriceDuplicates(prices)) {
            throw new ProductServiceValidationException("Price duplicates (with same locale) found.");
        }

        if (discountPrices != null) {
            if (!PriceValidator.isDiscountsValid(prices, discountPrices)) {
                throw new ProductServiceValidationException("Discount prices are invalid. " +
                        "Each discount price must have normal price in same region.");
            }
        }
    }

    private Product findProductById(final String productId) throws ProductServiceNotFoundException {
        return productRepository
                .findById(UUID.fromString(productId))
                .orElseThrow(() ->
                        new ProductServiceNotFoundException("Unable to get product with UUID" + productId)
                );
    }

    private String getSupplierNameByUserId(final UUID userId) {
        String supplierName = userService.getCompanyData(userId).getCompanyName();
        if (supplierName == null) {
            supplierName = userService.getPersonData(userId).getNickName();
        }
        return supplierName;
    }
}
