package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.Instant;

/**
 * Class that defines a discount for a specific product price.
 * Discounted price is always a temporary price.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double discountPrice;
    private Instant startInstant;
    private Instant endInstant;

    @OneToOne
    @JoinColumn(name = "product_price_id")
    private ProductPrice productPrice;
}
