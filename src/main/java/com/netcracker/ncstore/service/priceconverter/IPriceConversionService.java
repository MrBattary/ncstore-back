package com.netcracker.ncstore.service.priceconverter;

import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.UCPriceConvertedFromRealDTO;

import java.util.Locale;

/**
 * Interface for all services that convert universal coins (UC) to regional price.
 * Methods of this service must be used only for creating responses, as all prices in backend should be in UC
 */
public interface IPriceConversionService {

    /**
     * Converts priceInUC in UC to real priceInUC in specified region and also adds currency symbol.
     * If specified region is not present, returns priceInUC in default locale.
     * Also, returned price is rounded to two decimal places
     *
     * @param priceInUC - priceInUC in UC which will be converted
     * @param region - to which regional priceInUC UC value will be converted
     * @return converted UC value for specified region with Currency symbol for taht region
     */
    ConvertedPriceWithCurrencySymbolDTO convertUCPriceToRealPriceWithSymbol(double priceInUC, Locale region);

    /**
     * Converts price in specified region back to UC
     *
     * @param realPrice - regional price in real currency
     * @param regionOfPrice - region
     * @return DTO containing converted price value, requested locale and actual locale. Actual locale is different if no conversion rate for that locale specified.
     */
    UCPriceConvertedFromRealDTO convertRealPriceToUC(double realPrice, Locale regionOfPrice);

    /**
     * Converts ActualProductPriceInRegionDTO to ActualProductPriceConvertedForRegionDTO
     * which means converting normal and discount prices and adding currency symbol.
     *
     * @param actualProductPriceInRegionDTO - ActualProductPriceInRegionDTO
     * @return ActualProductPriceConvertedForRegionDTO containing converted prices
     */
    ActualProductPriceConvertedForRegionDTO convertActualUCPriceForRealPrice(ActualProductPriceInRegionDTO actualProductPriceInRegionDTO);
}
