package com.netcracker.ncstore.model;

import com.netcracker.ncstore.model.enumerations.EOrderProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Locale;

/**
 * Class that defines a single product item in order.
 * @author Artem Bakin
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private long id;
    private long orderId;
    private long productId;
    private double price;
    private Locale priceLocale;
    private String licenseKey;
    private EOrderProductStatus status;
}
