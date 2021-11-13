package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.ActualProductPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.exception.ProvidedLocaleIsNotValidException;
import com.netcracker.ncstore.exception.ProvidedPriceIsNegativeException;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import com.netcracker.ncstore.util.validator.LocaleValidator;
import com.netcracker.ncstore.util.validator.PriceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service responsible for any logic related to Price entity.
 * Is a default implementation of IPricesService.
 */
@Service
public class PricesService implements IPricesService {
    private final ProductPriceRepository productPriceRepository;
    private final Logger log;
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    public PricesService(final ProductPriceRepository productPriceRepository) {
        this.productPriceRepository = productPriceRepository;
        log = LoggerFactory.getLogger(PricesService.class);
    }

    @Override
    public ActualProductPriceWithCurrencySymbolDTO getActualPriceForProductInRegion(final ProductLocaleDTO productLocale) {
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

        return new ActualProductPriceWithCurrencySymbolDTO(
                productLocale.getProductId(),
                productPrice.getProduct().getName(),
                productPrice.getPrice(),
                discountPrice,
                Currency.getInstance(productLocale.getLocale()).
                        getSymbol(productLocale.getLocale()));
    }

    @Override
    public ProductPriceDTO createProductPrice(ProductPriceCreateDTO productPriceCreateDTO) {
        if (!PriceValidator.validatePricesValue(productPriceCreateDTO.getPrice())) {
            throw new ProvidedPriceIsNegativeException("Provided price is negative");
        }
        if (!LocaleValidator.isLocaleValid(productPriceCreateDTO.getRegion())) {
            throw new ProvidedLocaleIsNotValidException("Provided locale is invalid");
        }

        ProductPrice productPrice = productPriceRepository.save(new ProductPrice(null,
                productPriceCreateDTO.getPrice(),
                productPriceCreateDTO.getRegion(),
                productPriceCreateDTO.getProduct(),
                null));

        return new ProductPriceDTO(productPrice);
    }

    @Override
    public List<ProductPriceDTO> getPricesForProduct(UUID productId) {
        return productPriceRepository.findByProductId(productId).
                stream().
                map(ProductPriceDTO::new).
                collect(Collectors.toList());
    }
}
