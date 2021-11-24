package com.netcracker.ncstore.service.priceconverter;

import com.netcracker.ncstore.dto.PriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.exception.PriceConversionServiceLocaleNotSpecifiedException;

import java.util.Locale;

/**
 * Interface for all services that convert universal coins (UC) to regional price.
 */
public interface IPriceConversionService {

    /**
     * Converts priceInUC in UC to real priceInUC in specified region.
     * If specified region is not present, returns priceInUC in default locale.
     *
     * @param priceInUC - priceInUC in UC which will be converted
     * @param region - to which regional priceInUC UC value will be converted
     * @return converted UC value for specified region
     */
    double convertUCPriceToRealPrice(double priceInUC, Locale region);

    /**
     * Converts priceInUC in UC to real priceInUC in specified region and also adds currency symbol.
     * If specified region is not present, returns priceInUC in default locale.
     * Also, returned price is rounded to two decimal places
     *
     * @param priceInUC - priceInUC in UC which will be converted
     * @param region - to which regional priceInUC UC value will be converted
     * @return converted UC value for specified region with Currency symbol for taht region
     */
    PriceWithCurrencySymbolDTO convertUCPriceToRealPriceWithSymbol(double priceInUC, Locale region);

    /**
     * Converts price in specified region back to UC
     *
     * @param realPrice - regional price in real currency
     * @param regionOfPrice - region
     * @return converted price value
     * @throws PriceConversionServiceLocaleNotSpecifiedException - when provided locale is not registered in database, and it is impossible to convert provided real price to UC price
     */
    double convertRealPriceToUC(double realPrice, Locale regionOfPrice) throws PriceConversionServiceLocaleNotSpecifiedException;
}
