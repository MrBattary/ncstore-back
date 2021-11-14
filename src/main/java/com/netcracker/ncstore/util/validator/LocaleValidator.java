package com.netcracker.ncstore.util.validator;

import java.util.Arrays;
import java.util.Currency;
import java.util.Locale;

public abstract class LocaleValidator {

    public static boolean isLocaleValid(Locale locale) {
        boolean isValid;

        isValid = Arrays.stream(Locale.getISOCountries()).anyMatch(e -> e.equals(locale.getCountry()));
        isValid = isValid && Arrays.stream(Locale.getISOLanguages()).anyMatch(e -> e.equals(locale.getLanguage()));

        try {
            Currency.getInstance(locale).getSymbol(locale);
        } catch (Exception e) {
            isValid = false;
        }

        return isValid;
    }
}
