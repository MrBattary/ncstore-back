package com.netcracker.ncstore.util.converter;

import com.netcracker.ncstore.exception.RequestParametersInvalidException;
import com.netcracker.ncstore.util.enumeration.ESortOrder;
import com.netcracker.ncstore.util.enumeration.ESortRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class should contain all methods needed to convert
 * any Product request data to application data.
 * Methods must be static as this class can not have state
 * and there is no need to create instance of it
 */
public abstract class ProductRequestConverter {
    public static List<UUID> convertCategoriesStringToList(String stringOfCategoriesIDs) {
        if (!stringOfCategoriesIDs.equals("")) {
            try {
                return Arrays.stream(stringOfCategoriesIDs.split("\\|")).
                        map(UUID::fromString).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new RequestParametersInvalidException("Provided UUID is not valid.");
            }
        } else {
            return new ArrayList<>();
        }
    }

    public static ESortRule convertSortRuleStringToEnum(String sortRuleString) {
        try {
            return ESortRule.valueOf(sortRuleString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new RequestParametersInvalidException("Provided sort rule not supported.");
        }
    }

    public static ESortOrder convertSortOrderStringToEnum(String sortOrderString) {
        try {
            return ESortOrder.valueOf(sortOrderString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new RequestParametersInvalidException("Provided sort order not supported.");
        }
    }
}
