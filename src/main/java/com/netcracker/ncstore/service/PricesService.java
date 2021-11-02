package com.netcracker.ncstore.service;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

/**
 * Service responsible for any logic related to Price entity.
 */
@Service
public class PricesService {
    private ProductPriceRepository productPriceRepository;
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    public PricesService(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    public ProductPriceInRegionDTO getPriceForProductInRegion(ProductLocaleDTO productLocale){
        ProductPrice productPrice =
                productPriceRepository.findByProduct_IdAndLocale(productLocale.getProductId(),
                                                                 productLocale.getLocale());

        if(productPrice==null) {
            productPrice = productPriceRepository.findByProduct_IdAndLocale(productLocale.getProductId(),
                    Locale.forLanguageTag(defaultLocaleCode));
        }

        Double discountPrice = null;
        if(productPrice.getDiscount()!=null){
            Discount discount = productPrice.getDiscount();
            if((discount.getStartUtcTime().compareTo(Instant.now()) <= 0)
                    && (discount.getEndUtcTime().compareTo(Instant.now()) <= 0 )){
                discountPrice = discount.getDiscountPrice();
            }
        }

        return new ProductPriceInRegionDTO(
                productLocale.getProductId(),
                productPrice.getPrice(),
                discountPrice,
                productLocale.getLocale());
    }

    //TODO: function to check if price for specified product exists for specified locale
}
