package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.ProductsPageRequestDTO;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.repository.specification.ProductSpecifications;
import com.netcracker.ncstore.service.product.interfaces.IProductDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.UUID;

@Service
@Slf4j
public class ProductsDataService implements IProductDataService {
    private final ProductRepository productRepository;

    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    public ProductsDataService(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> getPageOfProductsUsingFilterAndSortParameters(ProductsPageRequestDTO productsPageRequest) {
        Pageable productsPageable;
        Page<Product> productsPage;

        productsPageable = PageRequest.of(
                productsPageRequest.getPage(),
                productsPageRequest.getSize()
        );

        Specification<Product> specification =
                ProductSpecifications.getByLikeName(productsPageRequest.getSearchText()).
                        and(
                                ProductSpecifications.getByCategoriesNames(productsPageRequest.getCategoriesNames())
                        ).
                        and(
                                ProductSpecifications.getByProductStatus(EProductStatus.IN_STOCK)
                        ).
                        and(
                                ProductSpecifications.getBySupplierId(productsPageRequest.getSupplierId())
                        ).
                        and(
                                ProductSpecifications.order(
                                        productsPageRequest.getSortOrder(),
                                        productsPageRequest.getSort(),
                                        productsPageRequest.getLocale(),
                                        Locale.forLanguageTag(defaultLocaleCode))
                        );

        return productRepository.findAll(specification, productsPageable);
    }

    @Override
    public boolean doesProductExist(UUID productId) {
        return productRepository.existsById(productId);
    }

    @Override
    public boolean checkIfProductIsOnSale(UUID id) {
        return doesProductExist(id) && productRepository.getById(id).getProductStatus().equals(EProductStatus.IN_STOCK);
    }

    @Override
    public Product loadProductEntityById(UUID id) throws ProductServiceNotFoundException {
        return productRepository.
                findById(id).
                orElseThrow(
                        () -> new ProductServiceNotFoundException("Product with UUID " + id + " not found. ")
                );
    }
}
