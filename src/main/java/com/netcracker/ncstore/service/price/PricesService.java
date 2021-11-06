package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ProductPriceInRegionDTO;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Currency;
import java.util.Locale;

/**
 * Service responsible for any logic related to Price entity.
 * Is a default implementation of IPricesService.
 */
@Service
public class PricesService implements IPricesService {
    private final ProductPriceRepository productPriceRepository;
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    public PricesService(final ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    @Override
    public ProductPriceInRegionDTO getPriceForProductInRegion(final ProductLocaleDTO productLocale) {
        ProductPrice productPrice = productPriceRepository.findByProductIDAndLocale(productLocale.getProductId(),
                productLocale.getLocale());

        if (productPrice == null) {
            productPrice = productPriceRepository.findByProductIDAndLocale(productLocale.getProductId(),
                    Locale.forLanguageTag(defaultLocaleCode));
        }

        Double discountPrice = null;
        if (productPrice.getDiscount() != null) {
            Discount discount = productPrice.getDiscount();
            if ((discount.getStartUtcTime().compareTo(Instant.now()) <= 0) &&
                    (discount.getEndUtcTime().compareTo(Instant.now()) >= 0)) {
                discountPrice = discount.getDiscountPrice();
            }
        }

        return new ProductPriceInRegionDTO(
                productLocale.getProductId(),
                productPrice.getProduct().getName(),
                productPrice.getPrice(),
                discountPrice,
                Currency.getInstance(productLocale.getLocale()).
                        getSymbol(productLocale.getLocale()));
    }
}
