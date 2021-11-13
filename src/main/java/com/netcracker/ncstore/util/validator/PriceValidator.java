package com.netcracker.ncstore.util.validator;

/**
 * Used for checking price.
 */
public abstract class PriceValidator {

    public static boolean validatePricesValue(double price){
        return price >= 0;
    }
}
