package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.OrderItem;
import com.netcracker.ncstore.model.enumerations.EOrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.UUID;

/**
 * Contains all information about real OrderItem entity,
 * as it is represented in database.
 * <p>
 * Used to safely transfer entity data between parts of the Program
 */
@AllArgsConstructor
@Getter
public class OrderItemDTO {
    private UUID id;
    private double price;
    private Locale priceLocale;
    private String licenseKey;
    private UUID orderId;
    private UUID productId;
    private EOrderItemStatus itemStatus;

    public OrderItemDTO(OrderItem orderItem) {
        id = orderItem.getId();
        price = orderItem.getPrice();
        priceLocale = orderItem.getPriceLocale();
        licenseKey = orderItem.getLicenseKey();
        orderId = orderItem.getId();
        productId = orderItem.getProduct().getId();
        itemStatus = orderItem.getItemStatus();
    }
}
