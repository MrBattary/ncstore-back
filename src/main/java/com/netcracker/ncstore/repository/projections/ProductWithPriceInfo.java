package com.netcracker.ncstore.repository.projections;

import com.netcracker.ncstore.model.Discount;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

public interface ProductWithPriceInfo {
    UUID getId();

    UUID getUserId();

    String getName();

    List<ProductPriceInfo> getProductPrices();

    interface ProductPriceInfo {
        double getPrice();

        Locale getLocale();

        Discount getDiscount();
    }
}
