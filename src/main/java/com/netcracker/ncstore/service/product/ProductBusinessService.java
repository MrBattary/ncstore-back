package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.ProductCreateDTO;
import com.netcracker.ncstore.dto.ProductDiscontinueDTO;
import com.netcracker.ncstore.dto.ProductPricesPopulateProductDTO;
import com.netcracker.ncstore.dto.ProductUpdateDTO;
import com.netcracker.ncstore.exception.CategoryServiceNotFoundException;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundExpectedException;
import com.netcracker.ncstore.exception.ProductServicePermissionException;
import com.netcracker.ncstore.exception.ProductServiceValidationException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.model.enumerations.ERoleName;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.service.category.interfaces.ICategoryBusinessService;
import com.netcracker.ncstore.service.price.interfaces.IPricesBusinessService;
import com.netcracker.ncstore.service.product.interfaces.IProductBusinessService;
import com.netcracker.ncstore.util.validator.PriceValidator;
import com.netcracker.ncstore.util.validator.ProductValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductBusinessService implements IProductBusinessService {
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    private final ProductRepository productRepository;
    private final ICategoryBusinessService categoryBusinessService;
    private final IPricesBusinessService pricesBusinessService;

    public ProductBusinessService(final ProductRepository productRepository,
                                  final ICategoryBusinessService categoryBusinessService,
                                  final IPricesBusinessService pricesBusinessService) {
        this.productRepository = productRepository;
        this.categoryBusinessService = categoryBusinessService;
        this.pricesBusinessService = pricesBusinessService;
    }

    @Override
    @Transactional
    public Product createProduct(ProductCreateDTO productCreateDTO)
            throws ProductServiceValidationException, ProductServicePermissionException,CategoryServiceNotFoundException,PricesServiceValidationException {
        try {
            User creator = productCreateDTO.getSupplier();
            log.info("Creation of new product for user with UUID " + creator.getId() + " begins");

            validateNewProductData(productCreateDTO);

            Product parentProduct = null;
            if (productCreateDTO.getParentProductUUID() != null) {
                parentProduct = productRepository.getById(productCreateDTO.getParentProductUUID());
            }

            List<Category> categories =
                    productCreateDTO.getCategoriesNames().
                            stream().
                            map(categoryBusinessService::getCategoryByName).
                            collect(Collectors.toList());

            Product product = productRepository.save(new Product(
                    productCreateDTO.getName(),
                    productCreateDTO.getDescription(),
                    parentProduct,
                    creator,
                    productCreateDTO.getStatus(),
                    categories)
            );

            pricesBusinessService.populateProductWithPrices(
                    new ProductPricesPopulateProductDTO(
                            productCreateDTO.getPrices(),
                            productCreateDTO.getDiscountPrices(),
                            product
                    )
            );

            log.info("New Product with UUID " + product.getId() + " for supplier with UUID " + creator.getId() + " created successfully");
            return product;
        } catch (ProductServiceValidationException
                | ProductServicePermissionException
                | CategoryServiceNotFoundException
                | PricesServiceValidationException e) {

            log.warn("Error while creating new product for user with UUID " + productCreateDTO.getSupplier().getId() + ". " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Product updateExistingProduct(ProductUpdateDTO productUpdateDTO)
            throws ProductServiceNotFoundException, ProductServicePermissionException, ProductServiceValidationException {
        try {
            log.info("Updating product UUID "+productUpdateDTO.getProductId()+"  by the request of the user with UUID " + productUpdateDTO.getIssuer().getId());

            Product productFromRepository = productRepository.
                    findById(productUpdateDTO.getProductId()).
                    orElseThrow(
                            () -> new ProductServiceNotFoundException("Product with UUID " + productUpdateDTO.getProductId() + " not found. ")
                    );

            validateUpdateProductData(productUpdateDTO);
            validateSupplierToModifyProduct(productUpdateDTO.getIssuer(), productFromRepository);

            Product parentProduct = null;
            if (productUpdateDTO.getGetParentProductId() != null) {
                parentProduct = productRepository.getById(productUpdateDTO.getGetParentProductId());
            }

            List<Category> categories =
                    productUpdateDTO.getCategoriesNames().
                            stream().
                            map(categoryBusinessService::getCategoryByName).
                            collect(Collectors.toList());

            productFromRepository.setName(productUpdateDTO.getName());
            productFromRepository.setDescription(productUpdateDTO.getDescription());
            productFromRepository.setParentProduct(parentProduct);
            productFromRepository.setCategories(categories);

            pricesBusinessService.deleteAllProductPricesForProduct(productFromRepository.getId());

            pricesBusinessService.populateProductWithPrices(
                    new ProductPricesPopulateProductDTO(
                            productUpdateDTO.getPrices(),
                            productUpdateDTO.getDiscountPrices(),
                            productFromRepository
                    )
            );

            log.info("Product with UUID " + productFromRepository.getId() + " for user with UUID "
                    + productUpdateDTO.getIssuer().getId() + " updated successfully. ");

            return productFromRepository;
        } catch (ProductServiceValidationException
                | CategoryServiceNotFoundException
                | PricesServiceValidationException e) {

            log.warn("Error while updating product with UUID " + productUpdateDTO.getProductId() + " for user with UUID " + productUpdateDTO.getIssuer().getId() + ". " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public Product discontinueProductSales(ProductDiscontinueDTO productDiscontinueDTO)
            throws ProductServiceNotFoundExpectedException, ProductServicePermissionException {
        try {
            log.info("Discontinuing product with UUID "+
                    productDiscontinueDTO.getProductId()+" by the request of the user with UUID " + productDiscontinueDTO.getIssuer().getId());

            Product productFromRepository = productRepository.
                    findById(productDiscontinueDTO.getProductId()).
                    orElseThrow(
                            () -> new ProductServiceNotFoundException("Product with UUID " + productDiscontinueDTO.getProductId() + " not found. ")
                    );

            validateSupplierToModifyProduct(productDiscontinueDTO.getIssuer(), productFromRepository);

            pricesBusinessService.deleteAllProductPricesForProduct(productFromRepository.getId());
            productFromRepository.setProductStatus(EProductStatus.DISCONTINUED);

            log.info("Successfully discontinued product with UUID "+productFromRepository.getId());

            return productFromRepository;

        } catch (ProductServiceValidationException
                | ProductServiceNotFoundException
                | IllegalArgumentException e) {

            log.warn("Can not discontinue product from store" + e.getMessage());
            throw e;
        }
    }

    private void validateNewProductData(ProductCreateDTO productCreateDTO) {
        validateUserAsSupplier(productCreateDTO.getSupplier());
        validateProductName(productCreateDTO.getName());
        validateProductDescription(productCreateDTO.getDescription());
        validateCategoriesNamesList(productCreateDTO.getCategoriesNames());
        validateParentProductId(productCreateDTO.getParentProductUUID());
        validateProductPrices(productCreateDTO.getPrices(), productCreateDTO.getDiscountPrices());
    }

    private void validateUpdateProductData(ProductUpdateDTO productUpdateDTO) {
        validateUserAsSupplier(productUpdateDTO.getIssuer());
        validateProductName(productUpdateDTO.getName());
        validateProductDescription(productUpdateDTO.getDescription());
        validateCategoriesNamesList(productUpdateDTO.getCategoriesNames());
        validateParentProductId(productUpdateDTO.getGetParentProductId());
        validateProductPrices(productUpdateDTO.getPrices(), productUpdateDTO.getDiscountPrices());
    }

    private void validateSupplierToModifyProduct(User supplier, Product product){
        if (!supplier.equals(product.getSupplier())) {
            throw new GeneralPermissionDeniedException("Only owner of product can update product. ");
        }
    }

    private void validateUserAsSupplier(User user) {
        if (user.getRoles().stream().noneMatch(e -> e.getRoleName() == ERoleName.SUPPLIER)) {
            throw new ProductServicePermissionException("Must have supplier role to create product. ");
        }
    }

    private void validateCategoriesNamesList(final List<String> categoriesNames) throws ProductServiceValidationException {
        if (!ProductValidator.checkCategoriesNamesList(categoriesNames)) {
            throw new ProductServiceValidationException("Categories list not provided or empty. ");
        }
    }

    private void validateParentProductId(final UUID id) throws ProductServiceValidationException {
        if (id != null) {
            if (productRepository.existsById(id)) {
                throw new ProductServiceValidationException("Product with UUID " + id
                        + " not found, but was requested as parent product for new product. ");
            }
        }
    }

    private void validateProductName(final String productName) throws ProductServiceValidationException {
        if (!ProductValidator.isNameValid(productName)) {
            throw new ProductServiceValidationException("Product name not provided or it length is not between (3;255). ");
        }
    }

    private void validateProductDescription(final String productDescription) throws ProductServiceValidationException {
        if (!ProductValidator.isDescriptionValid(productDescription)) {
            throw new ProductServiceValidationException("Product description not provided or is shorter than 50 symbols. ");
        }
    }

    private void validateProductPrices(final List<PriceRegionDTO> prices, final List<DiscountPriceRegionDTO> discountPrices)
            throws ProductServiceValidationException {
        if (prices == null) {
            throw new ProductServiceValidationException("Prices not specified. ");
        }

        boolean hasDefaultLocale = ProductValidator.hasProvidedLocale(
                prices.stream()
                        .map(PriceRegionDTO::getRegion)
                        .collect(Collectors.toList()), defaultLocaleCode);

        if (!hasDefaultLocale) {
            throw new ProductServiceValidationException("No price specified for the default locale with tag" + defaultLocaleCode + ". ");
        }

        if (!PriceValidator.isPriceDuplicates(prices)) {
            throw new ProductServiceValidationException("Price duplicates (with same locale) found. ");
        }

        if (discountPrices != null) {
            if (!PriceValidator.isDiscountsValid(prices, discountPrices)) {
                throw new ProductServiceValidationException("Discount prices are invalid. " +
                        "Each discount price must have normal price in same region. ");
            }
        }
    }
}
