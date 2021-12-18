package com.netcracker.ncstore.dto;

import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.util.validator.PriceValidator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public class ActualProductPrice {
    private final ProductPrice productPrice;
    //region in which price was requested
    private final Locale requestedRegion;
    //price in which region this DTO contains.
    //!= requestedRegion if there is no price for requested locale
    private final Locale actualRegion;
}
