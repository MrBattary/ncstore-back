package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ProductsGetRequestDTO;
import com.netcracker.ncstore.dto.api.ProductsGetResponse;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.service.price.PricesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for any logic related to Product entity.
 * Is a default implementation of IPricesService.
 */
@Service
public class ProductsService implements IProductsService {
    private final ProductRepository productRepository;
    private final PricesService pricesService;

    public ProductsService(final ProductRepository productRepository,
                           final PricesService pricesService) {
        this.productRepository = productRepository;
        this.pricesService = pricesService;
    }

    @Override
    public ProductsGetResponse getPageOfProductsByNameAndCategories(final ProductsGetRequestDTO productsGetRequestDTO) {
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

        List<ProductPriceInRegionDTO> productPriceInRegionDTOS =
                new ArrayList<>();

        for (Product product : productsPage.getContent()) {
            ProductLocaleDTO productLocaleDTO =
                    new ProductLocaleDTO(product.getId(), productsGetRequestDTO.getLocale());

            ProductPriceInRegionDTO priceInRegion =
                    pricesService.getPriceForProductInRegion(productLocaleDTO);

            productPriceInRegionDTOS.add(priceInRegion);
        }

        return new ProductsGetResponse(productPriceInRegionDTOS);
    }
}
