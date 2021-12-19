package com.netcracker.ncstore.util.converter;

import java.util.Currency;
import java.util.Locale;

public abstract class LocaleToCurrencyConverter {
    public static Currency getCurrencyByLocale(Locale locale) {
        return Currency.getInstance(locale);
    }

    public static String getCurrencySymbolByLocale(Locale locale) {
        return getCurrencyByLocale(locale).getSymbol(locale);
    }
}
