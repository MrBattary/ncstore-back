package com.netcracker.ncstore.service.discount;

import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.data.DiscountDTO;
import com.netcracker.ncstore.exception.DiscountServiceNotFoundException;
import com.netcracker.ncstore.exception.DiscountServiceValidationException;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.DiscountRepository;
import com.netcracker.ncstore.repository.ProductPriceRepository;
import com.netcracker.ncstore.util.validator.PriceValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class DiscountService implements IDiscountsService {
    private final DiscountRepository discountRepository;
    private final ProductPriceRepository productPriceRepository;
    private final Logger log;

    public DiscountService(DiscountRepository discountRepository, ProductPriceRepository productPriceRepository) {
        this.discountRepository = discountRepository;
        this.productPriceRepository = productPriceRepository;
        this.log = LoggerFactory.getLogger(DiscountService.class);
    }


    @Override
    public DiscountDTO createNewDiscountForPrice(DiscountCreateDTO discountCreateDTO) throws DiscountServiceValidationException {
        if (!PriceValidator.validatePricesValue(discountCreateDTO.getDiscountPrice())) {
            throw new DiscountServiceValidationException("Provided price is not valid");
        }

        ProductPrice productPrice = productPriceRepository.findById(discountCreateDTO.getProductPriceId()).orElse(null);

        if (productPrice == null) {
            throw new DiscountServiceValidationException("Provided product price does not exist");
        }
        if (productPrice.getDiscount() != null) {
            if (productPrice.getDiscount().getEndUtcTime().compareTo(Instant.now()) >= 0) {
                throw new DiscountServiceValidationException("Discount for this product already exists and is active.");
            }
        }

        Discount discount = discountRepository.save(new Discount(
                null,
                discountCreateDTO.getDiscountPrice(),
                discountCreateDTO.getStartUtcTime(),
                discountCreateDTO.getEndUtcTime(),
                productPrice));

        return new DiscountDTO(discount);
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
