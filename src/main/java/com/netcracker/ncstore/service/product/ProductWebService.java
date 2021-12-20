package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.ProductCreateDTO;
import com.netcracker.ncstore.dto.ProductDiscontinueDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductStatisticsReportDTO;
import com.netcracker.ncstore.dto.ProductUpdateDTO;
import com.netcracker.ncstore.dto.ProductsPageRequestDTO;
import com.netcracker.ncstore.dto.request.ProductCreateRequest;
import com.netcracker.ncstore.dto.request.ProductDeleteRequest;
import com.netcracker.ncstore.dto.request.ProductGetDetailedRequest;
import com.netcracker.ncstore.dto.request.ProductGetInfoRequest;
import com.netcracker.ncstore.dto.request.ProductGetPaginationRequest;
import com.netcracker.ncstore.dto.request.ProductGetStatisticsRequest;
import com.netcracker.ncstore.dto.request.ProductUpdateRequest;
import com.netcracker.ncstore.dto.response.ProductCreateResponse;
import com.netcracker.ncstore.dto.response.ProductDeleteResponse;
import com.netcracker.ncstore.dto.response.ProductGetDetailedResponse;
import com.netcracker.ncstore.dto.response.ProductGetInfoResponse;
import com.netcracker.ncstore.dto.response.ProductGetStatisticsResponse;
import com.netcracker.ncstore.dto.response.ProductUpdateResponse;
import com.netcracker.ncstore.dto.response.ProductsGetPaginationResponse;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.exception.ProductServicePermissionException;
import com.netcracker.ncstore.exception.ProductServiceValidationException;
import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.exception.general.GeneralNotFoundException;
import com.netcracker.ncstore.exception.general.GeneralPermissionDeniedException;
import com.netcracker.ncstore.model.Category;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.service.priceconverter.interfaces.IPriceConversionService;
import com.netcracker.ncstore.service.product.interfaces.IProductBusinessService;
import com.netcracker.ncstore.service.product.interfaces.IProductDataService;
import com.netcracker.ncstore.service.product.interfaces.IProductWebService;
import com.netcracker.ncstore.service.statistics.interfaces.IProductStatisticsService;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import com.netcracker.ncstore.util.converter.DoubleRounder;
import com.netcracker.ncstore.util.converter.ProductRequestConverter;
import com.netcracker.ncstore.util.enumeration.EProductSortRule;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductWebService implements IProductWebService {
    private final IProductBusinessService productsBusinessService;
    private final IProductDataService productDataService;
    private final IUserDataService userDataService;
    private final IPriceConversionService priceConversionService;
    private final IProductStatisticsService productStatisticsService;

    public ProductWebService(final IProductBusinessService productsBusinessService,
                             final IProductDataService productDataService,
                             final IUserDataService userDataService,
                             final IPriceConversionService priceConversionService,
                             final IProductStatisticsService productStatisticsService) {
        this.productsBusinessService = productsBusinessService;
        this.productDataService = productDataService;
        this.userDataService = userDataService;
        this.priceConversionService = priceConversionService;
        this.productStatisticsService = productStatisticsService;
    }


    @Override
    public List<ProductsGetPaginationResponse> getPageOfFilteredAndSortedProducts(ProductGetPaginationRequest request) {
        EProductSortRule sortEnum = ProductRequestConverter.convertSortRuleStringToEnum(request.getSort());
        ESortOrder sortOrderEnum = ProductRequestConverter.convertSortOrderStringToEnum(request.getSortOrder());

        ProductsPageRequestDTO productsPageRequestDTO = new ProductsPageRequestDTO(
                request.getCategoriesNames(),
                request.getSearchText(),
                request.getPage(),
                request.getSize(),
                request.getLocale(),
                sortEnum,
                sortOrderEnum,
                request.getSupplierId()
        );


        Page<Product> productPage =
                productDataService.getPageOfProductsUsingFilterAndSortParameters(productsPageRequestDTO);

        List<ProductsGetPaginationResponse> responseList = new ArrayList<>();

        for (Product product : productPage.getContent()) {
            ActualProductPriceConvertedForRegionDTO convertedPrice = priceConversionService.getActualConvertedPriceForProductInRegion(
                    new ProductLocaleDTO(
                            product.getId(),
                            request.getLocale()
                    )
            );

            ProductsGetPaginationResponse response =
                    new ProductsGetPaginationResponse(
                            product.getId(),
                            product.getName(),
                            product.getSupplier().getId(),
                            userDataService.getSupplierNameByUserId(product.getSupplier().getId()),
                            DoubleRounder.round(convertedPrice.getNormalConvertedPrice(), 2),
                            DoubleRounder.round(convertedPrice.getDiscountConvertedPrice(), 2),
                            convertedPrice.getCurrencySymbol()
                    );

            responseList.add(response);
        }

        return responseList;
    }

    @Override
    public ProductCreateResponse createNewProductInStore(ProductCreateRequest request) {
        try {
            User supplier = userDataService.getUserByEmail(request.getEmailOfIssuer());

            ProductCreateDTO createDTO = new ProductCreateDTO(
                    supplier,
                    request.getProductName(),
                    request.getProductDescription(),
                    EProductStatus.IN_STOCK,
                    request.getNormalPrices(),
                    request.getDiscountPrices(),
                    request.getParentProductId(),
                    request.getCategoriesNames()
            );

            Product product = productsBusinessService.createProduct(createDTO);


            return new ProductCreateResponse(
                    product.getId(),
                    product.getSupplier().getId(),
                    product.getName(),
                    product.getDescription(),
                    convertProductPriceListToPriceRegionDTOList(product.getProductPrices()),
                    convertProductPriceListToDiscountPriceRegionDTOList(product.getProductPrices()),
                    product.getParentProduct() == null ? null : product.getParentProduct().getId(),
                    convertCategoryListToNameList(product.getCategories())
            );

        } catch (ProductServiceValidationException validationException) {
            throw new GeneralBadRequestException(validationException.getMessage(), validationException);
        }
    }

    @Override
    public ProductUpdateResponse updateExistingProduct(ProductUpdateRequest request)
            throws GeneralPermissionDeniedException, GeneralNotFoundException, GeneralBadRequestException {
        try {
            User supplier = userDataService.getUserByEmail(request.getEmailOfIssuer());

            ProductUpdateDTO updateDTO = new ProductUpdateDTO(
                    request.getProductId(),
                    supplier,
                    request.getProductName(),
                    request.getProductDescription(),
                    EProductStatus.IN_STOCK,
                    request.getNormalPrices(),
                    request.getDiscountPrices(),
                    request.getParentProductId(),
                    request.getCategoriesNames()
            );

            Product product = productsBusinessService.updateExistingProduct(updateDTO);

            return new ProductUpdateResponse(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    convertProductPriceListToPriceRegionDTOList(product.getProductPrices()),
                    convertProductPriceListToDiscountPriceRegionDTOList(product.getProductPrices()),
                    product.getParentProduct() == null ? null : product.getParentProduct().getId(),
                    convertCategoryListToNameList(product.getCategories())
            );

        } catch (ProductServicePermissionException permissionException) {
            throw new GeneralPermissionDeniedException(permissionException.getMessage(), permissionException);
        } catch (ProductServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        } catch (ProductServiceValidationException validationException) {
            throw new GeneralBadRequestException(validationException.getMessage(), validationException);
        }
    }

    @Override
    public ProductDeleteResponse deleteProductFromStore(ProductDeleteRequest request)
            throws GeneralPermissionDeniedException, GeneralNotFoundException {
        try {
            User supplier = userDataService.getUserByEmail(request.getEmailOfIssuer());

            ProductDiscontinueDTO productDiscontinueDTO = new ProductDiscontinueDTO(
                    request.getProductId(),
                    supplier
            );

            Product product = productsBusinessService.discontinueProductSales(productDiscontinueDTO);

            return new ProductDeleteResponse(product.getId(),
                    product.getName(),
                    product.getDescription(),
                    convertProductPriceListToPriceRegionDTOList(product.getProductPrices()),
                    convertProductPriceListToDiscountPriceRegionDTOList(product.getProductPrices()),
                    product.getParentProduct() == null ? null : product.getParentProduct().getId(),
                    convertCategoryListToNameList(product.getCategories())
            );

        } catch (ProductServicePermissionException permissionException) {
            throw new GeneralPermissionDeniedException(permissionException.getMessage(), permissionException);
        } catch (ProductServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public ProductGetInfoResponse getProductInfo(ProductGetInfoRequest request) {
        try {
            Product product = productDataService.getProductById(request.getProductId());

            ActualProductPriceConvertedForRegionDTO convertedPrice = priceConversionService.getActualConvertedPriceForProductInRegion(
                    new ProductLocaleDTO(
                            product.getId(),
                            request.getLocale()
                    )
            );

            return new ProductGetInfoResponse(
                    product.getId(),
                    product.getSupplier().getId(),
                    userDataService.getSupplierNameByUserId(product.getSupplier().getId()),
                    product.getName(),
                    product.getDescription(),
                    DoubleRounder.round(convertedPrice.getNormalConvertedPrice(), 2),
                    DoubleRounder.round(convertedPrice.getDiscountConvertedPrice(), 2),
                    convertedPrice.getCurrencySymbol(),
                    convertedPrice.getDiscountStartUtc(),
                    convertedPrice.getDiscountEndUtc(),
                    product.getParentProduct() == null ? null : product.getParentProduct().getId(),
                    convertCategoryListToNameList(product.getCategories())
            );
        } catch (ProductServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public ProductGetDetailedResponse getDetailedProductInfo(ProductGetDetailedRequest request) {
        try {
            User supplier = userDataService.getUserByEmail(request.getEmailOfIssuer());

            Product product = productDataService.getProductById(request.getProductId());

            if (!product.getSupplier().equals(supplier)) {
                throw new GeneralPermissionDeniedException("Only owner of product can view detailed info. ");
            }

            return new ProductGetDetailedResponse(
                    product.getId(),
                    product.getName(),
                    product.getSupplier().getId(),
                    userDataService.getSupplierNameByUserId(product.getSupplier().getId()),
                    product.getDescription(),
                    convertProductPriceListToPriceRegionDTOList(product.getProductPrices()),
                    convertProductPriceListToDiscountPriceRegionDTOList(product.getProductPrices()),
                    product.getParentProduct() == null ? null : product.getParentProduct().getId(),
                    convertCategoryListToNameList(product.getCategories())
            );

        } catch (ProductServiceNotFoundException notFoundException) {
            throw new GeneralNotFoundException(notFoundException.getMessage(), notFoundException);
        }
    }

    @Override
    public ProductGetStatisticsResponse getStatisticsForProduct(ProductGetStatisticsRequest request) {
        Product product = productDataService.getProductById(request.getProductId());

        if (!product.getSupplier().getEmail().equals(request.getEmailOfIssuer())) {
            throw new GeneralPermissionDeniedException("Only supplier of product can view statistics of that product. ");
        }

        ProductStatisticsReportDTO productStatisticsReportDTO =
                productStatisticsService.getStatisticsReportForProduct(request.getProductId());

        return new ProductGetStatisticsResponse(
                productStatisticsReportDTO.getTotalSalesAmount(),
                productStatisticsReportDTO.getTotalRevenue()
        );
    }

    private List<PriceRegionDTO> convertProductPriceListToPriceRegionDTOList(List<ProductPrice> productPrices) {
        return productPrices.
                stream().
                map(
                        e -> new PriceRegionDTO(
                                DoubleRounder.round(e.getPrice(), 2),
                                e.getLocale()
                        )
                ).
                collect(Collectors.toList());
    }

    private List<DiscountPriceRegionDTO> convertProductPriceListToDiscountPriceRegionDTOList(List<ProductPrice> productPrices) {
        return productPrices.
                stream().
                filter(e -> e.getDiscount() != null).
                map(
                        e -> new DiscountPriceRegionDTO(
                                DoubleRounder.round(e.getDiscount().getDiscountPrice(), 2),
                                e.getLocale(),
                                e.getDiscount().getStartUtcTime(),
                                e.getDiscount().getEndUtcTime()
                        )
                ).
                collect(Collectors.toList());
    }

    private List<String> convertCategoryListToNameList(List<Category> categoryList) {
        return categoryList.
                stream().
                map(Category::getName).
                collect(Collectors.toList());
    }
}
