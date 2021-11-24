package com.netcracker.ncstore.service.priceconverter;

import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.exception.PriceConversionServiceLocaleNotSpecifiedException;
import com.netcracker.ncstore.model.PriceConversionRate;
import com.netcracker.ncstore.repository.PriceConversionRateRepository;
import com.netcracker.ncstore.util.converter.DoubleRounder;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class PriceConversionService implements IPriceConversionService {
    private final PriceConversionRateRepository priceConversionRateRepository;
    private final Locale defaultLocale;

    private final int decimalPlacesRound = 2;

    public PriceConversionService(final PriceConversionRateRepository priceConversionRateRepository,
                                  final @Value("${locale.default.code}") String defaultLocaleCode) {
        this.priceConversionRateRepository = priceConversionRateRepository;
        defaultLocale = Locale.forLanguageTag(defaultLocaleCode);
    }

    @Override
    public ConvertedPriceWithCurrencySymbolDTO convertUCPriceToRealPriceWithSymbol(double priceInUC, Locale region) {
        PriceConversionRate conversionRate = priceConversionRateRepository.findByRegion(region);

        if (conversionRate == null) {
            conversionRate = priceConversionRateRepository.findByRegion(defaultLocale);
        }

        return new ConvertedPriceWithCurrencySymbolDTO(
                DoubleRounder.round((1 / conversionRate.getUniversalPriceValue()) * priceInUC, decimalPlacesRound),
                LocaleToCurrencyConverter.getCurrencySymbolByLocale(conversionRate.getRegion()),
                conversionRate.getRegion());
    }

    @Override
    public double convertRealPriceToUC(double realPrice, Locale regionOfPrice) throws PriceConversionServiceLocaleNotSpecifiedException {
        PriceConversionRate conversionRate = priceConversionRateRepository.findByRegion(regionOfPrice);
        if (conversionRate != null) {
            return conversionRate.getUniversalPriceValue() * realPrice;
        } else {
            throw new PriceConversionServiceLocaleNotSpecifiedException("Locale with tag " + regionOfPrice.toLanguageTag() + " is not supported. Unable to convert to UC price.");
        }
    }

    @Override
    public ActualProductPriceConvertedForRegionDTO convertActualUCPriceForRealPrice(ActualProductPriceInRegionDTO actualProductPriceInRegionDTO) {
        Locale preferLocale = actualProductPriceInRegionDTO.getRegion();
        Double discountPrice = actualProductPriceInRegionDTO.getDiscountPrice();

        if(discountPrice!=null){
            discountPrice = convertUCPriceToRealPriceWithSymbol(actualProductPriceInRegionDTO.getDiscountPrice(), preferLocale).getPrice();
        }

        ConvertedPriceWithCurrencySymbolDTO convertedNormalPrice =
                convertUCPriceToRealPriceWithSymbol(actualProductPriceInRegionDTO.getNormalPrice(), preferLocale);

        return new ActualProductPriceConvertedForRegionDTO(
                actualProductPriceInRegionDTO.getProductId(),
                actualProductPriceInRegionDTO.getProductName(),
                convertedNormalPrice.getPrice(),
                discountPrice,
                convertedNormalPrice.getLocale(),
                convertedNormalPrice.getSymbol());
    }
}
