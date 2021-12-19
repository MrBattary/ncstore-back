package com.netcracker.ncstore.service.price;

import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.ProductPricesPopulateProductDTO;
import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.create.DiscountedProductPriceCreateDTO;
import com.netcracker.ncstore.dto.create.ProductPriceCreateDTO;
import com.netcracker.ncstore.exception.PricesServiceValidationException;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.DiscountRepository;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import com.netcracker.ncstore.service.price.interfaces.IPricesBusinessService;
import com.netcracker.ncstore.util.validator.LocaleValidator;
import com.netcracker.ncstore.util.validator.PriceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PricesBusinessService implements IPricesBusinessService {
    private final ProductPriceRepository productPriceRepository;
    private final DiscountRepository discountRepository;

    public PricesBusinessService(final ProductPriceRepository productPriceRepository,
                                 final DiscountRepository discountRepository) {
        this.productPriceRepository = productPriceRepository;
        this.discountRepository = discountRepository;
    }


    @Override
    public ProductPrice createProductPrice(ProductPriceCreateDTO productPriceCreateDTO) throws PricesServiceValidationException {
        try {
            if (!PriceValidator.isPriceValid(productPriceCreateDTO.getPrice())) {
                throw new PricesServiceValidationException("Provided price is not valid. ");
            }
            if (!LocaleValidator.isLocaleValid(productPriceCreateDTO.getRegion())) {
                throw new PricesServiceValidationException("Provided locale is not valid. ");
            }

            return productPriceRepository.save(
                    new ProductPrice(
                            productPriceCreateDTO.getPrice(),
                            productPriceCreateDTO.getRegion(),
                            productPriceCreateDTO.getProduct()
                    )
            );

        } catch (PricesServiceValidationException e) {
            log.warn("Error while creating product price for product. " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Discount createDiscountForProduct(DiscountCreateDTO discountCreateDTO) throws PricesServiceValidationException {
        try {
            log.info("Creating discount for product with UUID "+discountCreateDTO.getProduct().getId() +" in region " + discountCreateDTO.getRegion());
            if (!PriceValidator.isPriceValid(discountCreateDTO.getDiscountPrice())) {
                throw new PricesServiceValidationException("Provided price is not valid. ");
            }

            ProductPrice productPrice = productPriceRepository.findByProductIDAndLocale(
                    discountCreateDTO.getProduct().getId(),
                    discountCreateDTO.getRegion()
            );

            if (productPrice == null) {
                throw new PricesServiceValidationException("Product price for provided product and locale does not exist. ");
            }
            if (productPrice.getDiscount() != null) {
                throw new PricesServiceValidationException("Discount for this product already exists. ");
            }

            log.info("Succesfully created discount for product with UUID "+discountCreateDTO.getProduct().getId());

            return discountRepository.save(
                    new Discount(
                            discountCreateDTO.getDiscountPrice(),
                            discountCreateDTO.getStartUtcTime(),
                            discountCreateDTO.getEndUtcTime(),
                            productPrice
                    )
            );

        } catch (PricesServiceValidationException e) {
            log.warn("Error while creating discount for product. " + e.getMessage());
            throw e;
        }
    }

    @Override
    @Transactional
    public ProductPrice createDiscountedProductPrice(DiscountedProductPriceCreateDTO discountedProductPriceCreateDTO) throws PricesServiceValidationException {
        ProductPrice productPrice = createProductPrice(
                new ProductPriceCreateDTO(
                        discountedProductPriceCreateDTO.getRegularPrice(),
                        discountedProductPriceCreateDTO.getRegion(),
                        discountedProductPriceCreateDTO.getProduct()
                )
        );

        Discount discount = createDiscountForProduct(
                new DiscountCreateDTO(
                        discountedProductPriceCreateDTO.getProduct(),
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
    @Transactional
    public List<ProductPrice> populateProductWithPrices(ProductPricesPopulateProductDTO dto) {
        List<ProductPrice> productPrices = new ArrayList<>();

        Map<Locale, DiscountPriceRegionDTO> localeDiscountPriceRegionDTOMap =
                dto.getDiscountPrices().
                        stream().
                        collect(
                                Collectors.toMap(DiscountPriceRegionDTO::getRegion, e -> e)
                        );

        for (PriceRegionDTO priceRegionDTO : dto.getRegularPrices()) {
            DiscountPriceRegionDTO discountPriceRegionDTO =
                    localeDiscountPriceRegionDTOMap.get(priceRegionDTO.getRegion());

            ProductPrice productPrice;

            if (discountPriceRegionDTO != null) {
                productPrice = createDiscountedProductPrice(
                        new DiscountedProductPriceCreateDTO(
                                dto.getProduct(),
                                priceRegionDTO.getRegion(),
                                priceRegionDTO.getPrice(),
                                discountPriceRegionDTO.getPrice(),
                                discountPriceRegionDTO.getStartUtcTime(),
                                discountPriceRegionDTO.getEndUtcTime()
                        )
                );
            } else {
                productPrice = createProductPrice(
                        new ProductPriceCreateDTO(
                                priceRegionDTO.getPrice(),
                                priceRegionDTO.getRegion(),
                                dto.getProduct()
                        )
                );
            }

            productPrices.add(productPrice);
        }

        return productPrices;
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
}
