package com.netcracker.ncstore.util.converter;

import com.netcracker.ncstore.exception.RequestParametersInvalidException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This class should contain all methods needed to convert
 * any Product request data to application data.
 * Methods must be static as this class can not have state
 * and there is no need to create instance of it
 */
public abstract class ProductRequestConverter {
    public static List<UUID> convertCategoriesStringToList(String stringOfCategoriesIDs){
        if (!stringOfCategoriesIDs.equals("")) {
            try {
                return Arrays.stream(stringOfCategoriesIDs.split("\\|")).
                        map(UUID::fromString).collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                throw new RequestParametersInvalidException();
            }
        } else {
            return new ArrayList<>();
        }
    }
}
