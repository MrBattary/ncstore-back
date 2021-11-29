package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.ActualProductPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.DiscountRepository;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import com.netcracker.ncstore.util.validator.LocaleValidator;
import com.netcracker.ncstore.util.validator.PriceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final Logger log;
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    public PricesService(final ProductPriceRepository productPriceRepository,
                         final ProductRepository productRepository,
                         final DiscountRepository discountRepository) {
        this.productPriceRepository = productPriceRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
        log = LoggerFactory.getLogger(PricesService.class);
    }

    @Override
    public ActualProductPriceInRegionDTO getActualPriceForProductInRegion(final ProductLocaleDTO productLocale) {
        Locale locale = productLocale.getLocale();

        ProductPrice productPrice = productPriceRepository.findByProductIDAndLocale(productLocale.getProductId(),
                locale);

        if (productPrice == null) {
            locale = Locale.forLanguageTag(defaultLocaleCode);
            productPrice = productPriceRepository.findByProductIDAndLocale(productLocale.getProductId(),
                    locale);
        }

        Double discountPrice = PriceValidator.getActualDiscountPrice(productPrice.getDiscount());

        return new ActualProductPriceInRegionDTO(
                productLocale.getProductId(),
                productPrice.getProduct().getName(),
                productPrice.getPrice(),
                discountPrice,
                productLocale.getLocale(),
                locale
        );
    }

    @Override
    public ProductPriceDTO createProductPrice(ProductPriceCreateDTO productPriceCreateDTO) {
        if (!PriceValidator.isPriceValid(productPriceCreateDTO.getPrice())) {
            throw new PricesServiceValidationException("Provided price is not valid");
        }
        if (!LocaleValidator.isLocaleValid(productPriceCreateDTO.getRegion())) {
            throw new PricesServiceValidationException("Provided locale is not valid");
        }

        Product product = productRepository.findById(productPriceCreateDTO.getProductId()).orElse(null);
        if (productPriceCreateDTO.getProductId() == null) {
            throw new PricesServiceValidationException("Provided product does not exist");
        }

        ProductPrice productPrice = productPriceRepository.save(new ProductPrice(null,
                productPriceCreateDTO.getPrice(),
                productPriceCreateDTO.getRegion(),
                product,
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

    @Override
    public List<PriceRegionDTO> getListOfPriceRegionDtoByListOfPrices(List<ProductPrice> productPrices) {
        List<PriceRegionDTO> priceRegionDTOList = new ArrayList<>();

        for (ProductPrice productPrice : productPrices) {
            priceRegionDTOList.add(new PriceRegionDTO(productPrice.getPrice(), productPrice.getLocale()));
        }
        return priceRegionDTOList;
    }

    @Override
    public List<DiscountPriceRegionDTO> getListOfDiscountPriceRegionDtoByListOfPrices(List<ProductPrice> productPrices) {
        List<DiscountPriceRegionDTO> discountPriceRegionDTOList = new ArrayList<>();

        for (ProductPrice productPrice : productPrices) {
            Discount discountPrice = productPrice.getDiscount();
            if (discountPrice != null) {
                discountPriceRegionDTOList.add(
                        new DiscountPriceRegionDTO(
                                discountPrice.getDiscountPrice(),
                                productPrice.getLocale(),
                                discountPrice.getStartUtcTime(),
                                discountPrice.getEndUtcTime()
                        )
                );
            }
        }
        return discountPriceRegionDTOList;
    }

    @Override
    public void deleteAllProvidedPrices(final List<ProductPrice> productPrices) {
        for (ProductPrice productPrice : productPrices) {
            if (productPrice.getDiscount() != null) {
                discountRepository.deleteDiscountById(productPrice.getDiscount().getId());
            }
            productPriceRepository.deleteProductPriceById(productPrice.getId());
        }
    }
}
