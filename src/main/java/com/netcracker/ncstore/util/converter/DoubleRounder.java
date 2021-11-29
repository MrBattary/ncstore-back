package com.netcracker.ncstore.util.converter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class DoubleRounder {
    /**
     * Used to round double value to N decimal places.
     *
     * @param value - double to be rounded
     * @param places - decimal places
     * @return rounded double
     * @throws IllegalArgumentException - when places < 0
     */
    public static double round(double value, int places) throws IllegalArgumentException {
        if (places < 0){
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
