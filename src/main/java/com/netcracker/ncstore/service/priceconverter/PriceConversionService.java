package com.netcracker.ncstore.service.priceconverter;

import com.netcracker.ncstore.dto.PriceWithCurrencySymbolDTO;
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
    public double convertUCPriceToRealPrice(double priceInUC, Locale region) {
        PriceConversionRate conversionRate = priceConversionRateRepository.findByRegion(region);

        if (conversionRate == null) {
            conversionRate = priceConversionRateRepository.findByRegion(defaultLocale);
        }

        return  (1 / conversionRate.getUniversalPriceValue())* priceInUC;
    }

    @Override
    public PriceWithCurrencySymbolDTO convertUCPriceToRealPriceWithSymbol(double priceInUC, Locale region) {
        return new PriceWithCurrencySymbolDTO(
                DoubleRounder.round(convertUCPriceToRealPrice(priceInUC,region), decimalPlacesRound),
                LocaleToCurrencyConverter.getCurrencySymbolByLocale(region));
    }

    @Override
    public double convertRealPriceToUC(double realPrice, Locale regionOfPrice) throws PriceConversionServiceLocaleNotSpecifiedException {
        PriceConversionRate conversionRate = priceConversionRateRepository.findByRegion(regionOfPrice);
        if(conversionRate!=null){
            return conversionRate.getUniversalPriceValue()*realPrice;
        }else {
            throw new PriceConversionServiceLocaleNotSpecifiedException("Locale with tag "+ regionOfPrice.toLanguageTag() + " is not supported. Unable to convert to UC price.");
        }
    }
}
