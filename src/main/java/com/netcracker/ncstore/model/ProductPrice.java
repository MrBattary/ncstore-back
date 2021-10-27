package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Locale;

/**
 * Class that defines a price of a specific product in specific region.
 * Needed because prices could be different for poor and rich countries.
 * Needed because prices are in different currencies.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double price;
    private Locale locale;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
