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
 */
@Service
public class ProductsService implements IProductsService {
    private ProductRepository productRepository;
    private PricesService pricesService;

    public ProductsService(ProductRepository productRepository, PricesService pricesService) {
        this.productRepository = productRepository;
        this.pricesService = pricesService;
    }

    /**
     * Returns list of DTOs containing information about
     * product(name, price, discount, currency) and paging data.
     *
     * @param productsGetRequestDTO dto containing needed information
     * @return list of DTOs
     */
    public ProductsGetResponseDTO getPageOfProductsByNameAndCategories(ProductsGetRequestDTO productsGetRequestDTO) {
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

        for (Product product : productsPage.getContent()) {//for performance, this could be done by using 1 huge sql request in products repository
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
