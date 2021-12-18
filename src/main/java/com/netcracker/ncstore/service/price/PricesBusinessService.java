package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.ActualProductPrice;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.create.DiscountedProductPriceCreateDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.dto.data.DiscountDTO;
import com.netcracker.ncstore.dto.data.ProductPriceDTO;
import com.netcracker.ncstore.exception.DiscountServiceNotFoundException;
import com.netcracker.ncstore.exception.DiscountServiceValidationException;
import com.netcracker.ncstore.exception.PricesServiceCreationException;
import com.netcracker.ncstore.exception.PricesServiceNotFoundException;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.DiscountRepository;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import com.netcracker.ncstore.repository.ProductRepository;
import com.netcracker.ncstore.service.price.interfaces.IPricesBusinessService;
import com.netcracker.ncstore.util.validator.LocaleValidator;
import com.netcracker.ncstore.util.validator.PriceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PricesBusinessService implements IPricesBusinessService {
    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    @Value("${locale.default.code}")
    private String defaultLocaleCode;

    public PricesBusinessService(final ProductPriceRepository productPriceRepository,
                                 final ProductRepository productRepository,
                                 final DiscountRepository discountRepository) {
        this.productPriceRepository = productPriceRepository;
        this.productRepository = productRepository;
        this.discountRepository = discountRepository;
    }


    @Override
    public ProductPrice createProductPrice(ProductPriceCreateDTO productPriceCreateDTO) throws PricesServiceCreationException {
        try {
            if (!PriceValidator.isPriceValid(productPriceCreateDTO.getPrice())) {
                throw new PricesServiceValidationException("Provided price is not valid. ");
            }
            if (!LocaleValidator.isLocaleValid(productPriceCreateDTO.getRegion())) {
                throw new PricesServiceValidationException("Provided locale is not valid. ");
            }

            //TODO refactor when product service refactored
            Product product = productRepository.findById(productPriceCreateDTO.getProductId()).orElse(null);
            if (productPriceCreateDTO.getProductId() == null) {
                throw new PricesServiceValidationException("Provided product does not exist. ");
            }

            return productPriceRepository.save(
                    new ProductPrice(
                            productPriceCreateDTO.getPrice(),
                            productPriceCreateDTO.getRegion(),
                            product
                    )
            );

        } catch (PricesServiceValidationException e) {
            throw new PricesServiceCreationException("Unable to create price for product. " + e.getMessage(), e);
        }
    }

    @Override
    public Discount createDiscountForProduct(DiscountCreateDTO discountCreateDTO) throws PricesServiceCreationException {
        try {
            if (!PriceValidator.isPriceValid(discountCreateDTO.getDiscountPrice())) {
                throw new DiscountServiceValidationException("Provided price is not valid. ");
            }

            ProductPrice productPrice = productPriceRepository.findByProductIDAndLocale(
                    discountCreateDTO.getProductId(),
                    discountCreateDTO.getRegion()
            );

            if (productPrice == null) {
                throw new DiscountServiceValidationException("Product price for provided product and locale does not exist. ");
            }
            if (productPrice.getDiscount() != null) {
                throw new DiscountServiceValidationException("Discount for this product already exists. ");
            }

            return discountRepository.save(
                    new Discount(
                            discountCreateDTO.getDiscountPrice(),
                            discountCreateDTO.getStartUtcTime(),
                            discountCreateDTO.getEndUtcTime(),
                            productPrice
                    )
            );
        } catch (DiscountServiceValidationException e) {
            throw new PricesServiceCreationException("Unable to create discount for product. " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public ProductPrice createDiscountedProductPrice(DiscountedProductPriceCreateDTO discountedProductPriceCreateDTO) throws PricesServiceCreationException {
        ProductPrice productPrice = createProductPrice(
                new ProductPriceCreateDTO(
                        discountedProductPriceCreateDTO.getRegularPrice(),
                        discountedProductPriceCreateDTO.getRegion(),
                        discountedProductPriceCreateDTO.getProductId()
                )
        );

        Discount discount = createDiscountForProduct(
                new DiscountCreateDTO(
                        discountedProductPriceCreateDTO.getProductId(),
                        discountedProductPriceCreateDTO.getRegion(),
                        discountedProductPriceCreateDTO.getDiscountPrice(),
                        discountedProductPriceCreateDTO.getStartUtcTime(),
                        discountedProductPriceCreateDTO.getEndUtcTime()
                )
        );

        productPrice.setDiscount(discount);

        return productPrice;
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

    @Override
    @Transactional
    public void deleteProductPrice(ProductPrice productPrice) {
        productPriceRepository.delete(productPrice);
    }

    @Override
    @Transactional
    public void deleteAllProductPricesForProduct(UUID productId) {
        productPriceRepository.deleteByProductId(productId);
    }

    @Override
    public List<ProductPriceDTO> getPricesForProduct(UUID productId) {
        return productPriceRepository.findByProductId(productId).
                stream().
                map(ProductPriceDTO::new).
                collect(Collectors.toList());
    }

    @Override
    public DiscountDTO getDiscountForPrice(UUID productPriceId) throws DiscountServiceNotFoundException {
        Discount discount = discountRepository.findById(productPriceId).orElse(null);
        if (discount == null) {
            throw new DiscountServiceNotFoundException();
        }
        return new DiscountDTO(discount);
    }
}
