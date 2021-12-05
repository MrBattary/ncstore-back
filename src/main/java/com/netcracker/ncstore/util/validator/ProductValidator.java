package com.netcracker.ncstore.util.validator;

import java.util.List;
import java.util.Locale;

/**
 * Used for product validation
 */
public abstract class ProductValidator {
    public static boolean isNameValid(String name) {
        boolean isValid;
        isValid = name != null;
        if (isValid) {
            isValid = name.length() >= 3 && name.length() <= 255;
        }
        return isValid;
    }

    public static boolean isDescriptionValid(String description) {
        boolean isValid;
        isValid = description != null;
        if (isValid) {
            isValid = description.length() > 50;
        }
        return isValid;
    }

    public static boolean checkCategoriesNamesList(List<String> categories) {
        return categories != null;
    }

    public static boolean hasProvidedLocale(List<Locale> locales, String defaultLocaleCode) {
        return locales.stream().anyMatch(e -> e.equals(Locale.forLanguageTag(defaultLocaleCode)));
    }
}
