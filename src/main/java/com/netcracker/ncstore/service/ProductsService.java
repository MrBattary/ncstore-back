package com.netcracker.ncstore.service;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ProductsGetRequestDTO;
import com.netcracker.ncstore.dto.ProductsGetResponseDTO;
import com.netcracker.ncstore.exception.ProductsPageNumberExceedsPageCountException;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.repository.ProductRepository;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Service
public class ProductsService {
    private ProductRepository productRepository;
    private PricesService pricesService;

    public List<ProductsGetResponseDTO> getPageOfProducts(ProductsGetRequestDTO productsGetRequestDTO){
        Pageable productsPageRequest =
                PageRequest.of(productsGetRequestDTO.getPage(), productsGetRequestDTO.getSize());

        Page<Product> productsPage = productRepository.findProductsByNameAndCategories(
                productsGetRequestDTO.getSearchText(),
                productsGetRequestDTO.getCategoriesIds(),
                productsPageRequest);

        if((productsGetRequestDTO.getPage()+1) >= productsPage.getTotalPages()) {
            throw new ProductsPageNumberExceedsPageCountException(productsGetRequestDTO.getPage(), productsPage.getTotalPages());
        }

        List<ProductsGetResponseDTO> returnDTOList = new ArrayList<>();

        for(Product product : productsPage.getContent()){//for performance, this could be done by using 1 huge sql request in products repository
            ProductLocaleDTO productLocaleDTO =
                    new ProductLocaleDTO(product.getId(), productsGetRequestDTO.getLocale());

            ProductPriceInRegionDTO priceInRegion =
                    pricesService.getPriceForProductInRegion(productLocaleDTO);

            ProductsGetResponseDTO productDTO = new ProductsGetResponseDTO(
                    product.getId(),
                    product.getName(),
                    priceInRegion.getNormalPrice(),
                    priceInRegion.getDiscountPrice(),
                    Currency.getInstance(priceInRegion.getLocale()).getSymbol(),
                    productsPage.getNumber(),
                    productsPage.getTotalPages()
            );

            returnDTOList.add(productDTO);
        }

        return returnDTOList;
    }
}
