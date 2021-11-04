package com.netcracker.ncstore.service;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ProductsGetRequestDTO;
import com.netcracker.ncstore.dto.ProductsGetResponseDTO;
import com.netcracker.ncstore.exception.ProductsPageNumberExceedsPageCountException;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

/**
 * Service responsible for any logic related to Product entity.
 * Is a default implementation of IPricesService.
 */
@Service
public class ProductsService implements IProductsService {
    private final ProductRepository productRepository;
    private final PricesService pricesService;

    public ProductsService(final ProductRepository productRepository, final PricesService pricesService) {
        this.productRepository = productRepository;
        this.pricesService = pricesService;
    }

    public ProductsGetResponseDTO getPageOfProductsByNameAndCategories(final ProductsGetRequestDTO productsGetRequestDTO) {
        Pageable productsPageRequest =
                PageRequest.of(productsGetRequestDTO.getPage(), productsGetRequestDTO.getSize());

        Page<Product> productsPage;

        if (productsGetRequestDTO.getCategoriesIds().size() != 0) {
            productsPage = productRepository.findProductsByLikeNameAndCategories(
                    productsGetRequestDTO.getSearchText(),
                    productsGetRequestDTO.getCategoriesIds(),
                    productsPageRequest);
        } else {
            productsPage = productRepository.findProductByLikeName(
                    productsGetRequestDTO.getSearchText(),
                    productsPageRequest);
        }

        if (productsGetRequestDTO.getPage() >= productsPage.getTotalPages()) {
            throw new ProductsPageNumberExceedsPageCountException(productsGetRequestDTO.getPage(), productsPage.getTotalPages());
        }

        List<ProductPriceInRegionDTO> productPriceInRegionDTOS =
                new ArrayList<>();

        for (Product product : productsPage.getContent()) {
            ProductLocaleDTO productLocaleDTO =
                    new ProductLocaleDTO(product.getId(), productsGetRequestDTO.getLocale());

            ProductPriceInRegionDTO priceInRegion =
                    pricesService.getPriceForProductInRegion(productLocaleDTO);

            productPriceInRegionDTOS.add(priceInRegion);
        }

        return new ProductsGetResponseDTO(
                productPriceInRegionDTOS,
                productsPage.getNumber(),
                productsPage.getTotalPages());
    }
}
