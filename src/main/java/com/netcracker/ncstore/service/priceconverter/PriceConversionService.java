package com.netcracker.ncstore.service.priceconverter;

import com.netcracker.ncstore.dto.ActualProductPrice;
import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.UCPriceConvertedFromRealDTO;
import com.netcracker.ncstore.exception.PriceConversionServiceNotFoundException;
import com.netcracker.ncstore.exception.PricesServiceNotFoundException;
import com.netcracker.ncstore.model.Discount;
import com.netcracker.ncstore.model.PriceConversionRate;
import com.netcracker.ncstore.model.ProductPrice;
import com.netcracker.ncstore.repository.PriceConversionRateRepository;
import com.netcracker.ncstore.service.price.interfaces.IPricesDataService;
import com.netcracker.ncstore.service.priceconverter.interfaces.IPriceConversionService;
import com.netcracker.ncstore.util.converter.DoubleRounder;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import com.netcracker.ncstore.util.validator.PriceValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@Slf4j
public class PriceConversionService implements IPriceConversionService {
    private final PriceConversionRateRepository priceConversionRateRepository;
    private final IPricesDataService pricesDataService;
    private final Locale defaultLocale;

    private final int decimalPlacesRound = 2;

    public PriceConversionService(final PriceConversionRateRepository priceConversionRateRepository,
                                  final IPricesDataService pricesDataService,
                                  final @Value("${locale.default.code}") String defaultLocaleCode) {
        this.priceConversionRateRepository = priceConversionRateRepository;
        this.pricesDataService = pricesDataService;
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
    public UCPriceConvertedFromRealDTO convertRealPriceToUC(double realPrice, Locale regionOfPrice) {
        PriceConversionRate conversionRate = priceConversionRateRepository.findByRegion(regionOfPrice);
        if (conversionRate != null) {
            return new UCPriceConvertedFromRealDTO(
                    conversionRate.getUniversalPriceValue() * realPrice,
                    regionOfPrice,
                    regionOfPrice
            );
        } else {
            conversionRate = priceConversionRateRepository.findByRegion(defaultLocale);
            return new UCPriceConvertedFromRealDTO(
                    conversionRate.getUniversalPriceValue() * realPrice,
                    regionOfPrice,
                    conversionRate.getRegion()
            );
        }
    }

    @Override
    public ActualProductPriceConvertedForRegionDTO getActualConvertedPriceForProductInRegion(ActualProductPrice actualProductPrice) {
        Locale region = actualProductPrice.getRequestedRegion();
        ProductPrice productPrice = actualProductPrice.getProductPrice();
        Discount discount = productPrice.getDiscount();

        Double discountPrice = PriceValidator.getActualDiscountPrice(discount);

        if (discountPrice != null) {
            discountPrice = convertUCPriceToRealPriceWithSymbol(discountPrice, region).getPrice();
        }

        ConvertedPriceWithCurrencySymbolDTO convertedNormalPrice =
                convertUCPriceToRealPriceWithSymbol(productPrice.getPrice(), region);

        return new ActualProductPriceConvertedForRegionDTO(
                productPrice.getProduct().getId(),
                productPrice.getProduct().getName(),
                convertedNormalPrice.getPrice(),
                discountPrice,
                convertedNormalPrice.getLocale(),
                convertedNormalPrice.getSymbol(),
                discount == null ? null : discount.getStartUtcTime(),
                discount == null ? null : discount.getEndUtcTime()
        );
    }

    @Override
    public ActualProductPriceConvertedForRegionDTO getActualConvertedPriceForProductInRegion(ProductLocaleDTO productLocaleDTO) throws PriceConversionServiceNotFoundException {
        try {
            ActualProductPrice actualProductPrice =
                    pricesDataService.getActualPriceForProductInRegion(productLocaleDTO);

            return getActualConvertedPriceForProductInRegion(actualProductPrice);
        }catch (PricesServiceNotFoundException notFoundException){
            throw new PriceConversionServiceNotFoundException("Can not convert price. "+ notFoundException.getMessage(), notFoundException);
        }

    }
}
