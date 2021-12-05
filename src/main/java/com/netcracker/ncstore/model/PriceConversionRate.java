package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Locale;
import java.util.UUID;

/**
 * Each product price should be represented in universal coins (UC).
 * This entity helps to convert UC to regular price in region.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PriceConversionRate {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private Locale region;
    /**
     * Defines how much money in this locale costs 1 unit of UC
     * Ex. №1 if 1 $ = 1 UC, then 1 should be here.
     * Ex. №2 if 1 € = 1.1 UC, then 1.1 should be here.
     * Ex. №3 if 1 ₽ = 0.013 UC, then 0.013 should be here
     */
    private double universalPriceValue;

    public Locale getRegion() {
        return region;
    }
}