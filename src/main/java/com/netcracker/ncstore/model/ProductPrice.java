package com.netcracker.ncstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Locale;
import java.util.UUID;

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
    @GeneratedValue
    private UUID id;
    private double price;
    private Locale locale;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(mappedBy = "productPrice")
    private Discount discount;

    @Basic(fetch = FetchType.LAZY)
    @Formula(value = "coalesce((select d.discount_price " +
            "from discount d " +
            "where d.product_price_id = id " +
            "and d.start_utc_time < timezone('utc', now()) " +
            "and d.end_utc_time > timezone('utc', now()))" +
            ", price)")
    private double priceWithDiscount;

    @Basic(fetch = FetchType.LAZY)
    @Formula(value = "coalesce((1 - (" +
            "(select d.discount_price " +
            "from discount d " +
            "where d.product_price_id = id " +
            "and d.start_utc_time < timezone('utc', now()) " +
            "and d.end_utc_time > timezone('utc', now()))" +
            "/price)), 0)")
    private double discountPercent;

    public ProductPrice(UUID id, double price, Locale locale, Product product, Discount discount) {
        this.id = id;
        this.price = price;
        this.locale = locale;
        this.product = product;
        this.discount = discount;
    }
}
