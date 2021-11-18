package com.netcracker.ncstore.service.discount;

import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
import com.netcracker.ncstore.dto.data.DiscountDTO;
import com.netcracker.ncstore.exception.DiscountServiceNotFoundException;
import com.netcracker.ncstore.exception.DiscountServiceValidationException;

import java.util.UUID;

public interface IDiscountsService {
    /**
     * Creates new Discount entity based on provided parameters
     *
     * @param discountCreateDTO DiscountCreateDTO
     * @return DiscountDTO which represents created ProductPrice in a safe way
     */
    DiscountDTO createNewDiscountForPrice(final DiscountCreateDTO discountCreateDTO) throws DiscountServiceValidationException;

    /**
     * Returns discount for provided productPrice
     *
     * @param productPriceId DiscountDTO
     * @return DiscountDTO which represents real Discount entity in a safe way
     */
    DiscountDTO getDiscountForPrice(final UUID productPriceId) throws DiscountServiceNotFoundException;
}
