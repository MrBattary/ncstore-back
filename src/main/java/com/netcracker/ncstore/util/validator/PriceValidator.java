package com.netcracker.ncstore.util.validator;

import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;

import java.util.List;
import java.util.function.Predicate;

/**
 * Used for checking price.
 */
public abstract class PriceValidator {

    public static boolean validatePricesValue(double price) {
        return price >= 0;
    }

    public static boolean validateDiscounts(List<PriceRegionDTO> normalPrices, List<DiscountPriceRegionDTO> discounts) {
        if (discounts.size() > normalPrices.size()) {
            return false;
        }

        Predicate<DiscountPriceRegionDTO> checkDiscountRegionForExistenceInPrices =
                o -> normalPrices.stream().anyMatch(e -> e.getRegion().equals(o.getRegion()));

        return discounts.stream().allMatch(checkDiscountRegionForExistenceInPrices);
    }
}
