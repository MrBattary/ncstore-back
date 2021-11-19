package com.netcracker.ncstore.util.validator;

import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.model.Discount;

import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;

/**
 * Used for checking price.
 */
public abstract class PriceValidator {

    public static boolean isPriceValid(double price) {
        return price >= 0;
    }

    public static boolean isDiscountsValid(List<PriceRegionDTO> normalPrices, List<DiscountPriceRegionDTO> discounts) {
        if (discounts.size() > normalPrices.size()) {
            return false;
        }

        Predicate<DiscountPriceRegionDTO> checkDiscountRegionForExistenceInPrices =
                o -> normalPrices.stream().anyMatch(e -> e.getRegion().equals(o.getRegion()));

        return discounts.stream().allMatch(checkDiscountRegionForExistenceInPrices);
    }

    public static Double getActualDiscountPrice(Discount discount){
        if(discount==null){
            return null;
        }else if(discount.getStartUtcTime().compareTo(Instant.now()) > 0){
            return null;
        }else if(discount.getEndUtcTime().compareTo(Instant.now()) < 0){
            return null;
        }else {
            return discount.getDiscountPrice();
        }
    }
}
