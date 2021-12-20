package com.netcracker.ncstore.util.converter;

import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.util.enumeration.EProductSortRule;
import com.netcracker.ncstore.util.enumeration.ESortOrder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * This class should contain all methods needed to convert
 * any Product request data to application data.
 * Methods must be static as this class can not have state
 * and there is no need to create instance of it
 */
public abstract class ProductRequestConverter {
    public static List<String> convertCategoriesStringToList(String stringOfCategoriesNames) {
        if (!stringOfCategoriesNames.equals("")) {
            try {
                return Arrays.stream(stringOfCategoriesNames.split("\\|")).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new GeneralBadRequestException("Provided categories names are not valid.");
            }
        } else {
            return new ArrayList<>();
        }
    }

    public static EProductSortRule convertSortRuleStringToEnum(String sortRuleString) {
        try {
            return EProductSortRule.valueOf(sortRuleString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new GeneralBadRequestException("Provided sort rule " + sortRuleString + " not supported.");
        }
    }

    public static ESortOrder convertSortOrderStringToEnum(String sortOrderString) {
        try {
            return ESortOrder.valueOf(sortOrderString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new GeneralBadRequestException("Provided sort order " + sortOrderString + " not supported.");
        }
    }
}
