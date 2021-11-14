package com.netcracker.ncstore.service.product;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.request.ProductsGetRequest;
import com.netcracker.ncstore.dto.response.ProductsGetResponse;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.service.price.PricesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public ProductsGetResponse getPageOfProductsUsingFilterAndSortParameters(final ProductsGetRequest productsGetRequest) {
        Pageable productsPageRequest =
                PageRequest.of(productsGetRequest.getPage(), productsGetRequest.getSize());

        Page<Product> productsPage;

        switch (productsGetRequest.getSortString()){
            default:
            case "default":
            case "rating"://TODO separate and make when rating will be
                break;
            case "price":
                break;
            case "new":
                break;
            case "popular":
                break;
            case "discount":
                break;
        }

        if (productsGetRequest.getCategoriesIds().size() != 0) {
            productsPage = productRepository.findProductsByLikeNameAndCategories(
                    productsGetRequest.getSearchText(),
                    productsGetRequest.getCategoriesIds(),
                    productsPageRequest);
        } else {
            productsPage = productRepository.findProductByLikeName(
                    productsGetRequest.getSearchText(),
                    productsPageRequest);
        }

        productsPage.getContent().forEach(e-> System.out.println(e.getName()));

        List<ProductPriceInRegionDTO> productPriceInRegionDTOS =
                new ArrayList<>();

        for (Product product : productsPage.getContent()) {
            ProductLocaleDTO productLocaleDTO =
                    new ProductLocaleDTO(product.getId(), productsGetRequest.getLocale());

            ProductPriceInRegionDTO priceInRegion =
                    pricesService.getPriceForProductInRegion(productLocaleDTO);

            productPriceInRegionDTOS.add(priceInRegion);
        }

        return new ProductsGetResponse(productPriceInRegionDTOS);
    }
}
