package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.ActualProductPrice;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.exception.PricesServiceNotFoundException;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import com.netcracker.ncstore.service.price.interfaces.IPricesDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class PricesDataService implements IPricesDataService {
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    private final ProductPriceRepository productPriceRepository;

    public PricesDataService(ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
    }

    @Override
    public ActualProductPrice getActualPriceForProductInRegion(final ProductLocaleDTO productLocale) {
        Locale locale = productLocale.getLocale();

        ProductPrice productPrice = productPriceRepository.findByProductIDAndLocale(productLocale.getProductId(),
                locale);

        if (productPrice == null) {
            locale = Locale.forLanguageTag(defaultLocaleCode);
            productPrice = productPriceRepository.findByProductIDAndLocale(productLocale.getProductId(),
                    locale);
        }

        if(productPrice==null){
            throw new PricesServiceNotFoundException("Product does not exists or has no price for default locale. ");
        }

        return new ActualProductPrice(
                productPrice,
                productLocale.getLocale(),
                locale
        );
    }
}
