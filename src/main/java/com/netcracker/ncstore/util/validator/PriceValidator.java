package com.netcracker.ncstore.util.validator;

import com.netcracker.ncstore.dto.DiscountPriceRegionDTO;
import com.netcracker.ncstore.dto.PriceRegionDTO;
import com.netcracker.ncstore.dto.create.DiscountCreateDTO;
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
        if(discount==null) {
            return null;
        }
        return getActualDiscountPrice(discount.getStartUtcTime(), discount.getEndUtcTime(), discount.getDiscountPrice());
    }
    public static Double getActualDiscountPrice(DiscountCreateDTO discountCreateDTO){
        if(discountCreateDTO==null) {
            return null;
        }
        return getActualDiscountPrice(discountCreateDTO.getStartUtcTime(), discountCreateDTO.getEndUtcTime(), discountCreateDTO.getDiscountPrice());
    }

    public static Double getActualDiscountPrice(Instant startUtcTime, Instant endUtcTime, double price){
        if(startUtcTime.compareTo(Instant.now()) > 0){
            return null;
        }else if(endUtcTime.compareTo(Instant.now()) < 0){
            return null;
        }else {
            return price;
        }
    }

    public static boolean isPriceDuplicates(List<PriceRegionDTO> prices){
        Predicate<PriceRegionDTO> checkPriceDuplicate =
                priceRegionDTO -> prices.stream().filter(e->e.getRegion().equals(priceRegionDTO.getRegion())).count() > 1;

        return prices.stream().noneMatch(checkPriceDuplicate);
    }
}
