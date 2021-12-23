package com.netcracker.ncstore.util.converter;

import com.netcracker.ncstore.exception.general.GeneralBadRequestException;
import com.netcracker.ncstore.util.enumeration.EReviewSortRule;
import com.netcracker.ncstore.util.enumeration.ESortOrder;

import java.util.Locale;

/**
 * This class should contain all methods needed to convert
 * any ProductReview request data to application data.
 * Methods must be static as this class can not have state
 * and there is no need to create instance of it
 */
public abstract class ReviewRequestConverter {
    public static EReviewSortRule convertSortRuleStringToEnum(String sortRuleString) {
        try {
            return EReviewSortRule.valueOf(sortRuleString.toUpperCase(Locale.ROOT));
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
