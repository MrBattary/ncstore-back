package com.netcracker.ncstore.dto.data;

import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.model.OrderItem;
import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Contains all information about real Order entity,
 * as it is represented in database.
 * <p>
 * Used to safely transfer entity data between parts of the Program
 */
@AllArgsConstructor
@Getter
public class OrderDTO {
    private final UUID id;
    private final Instant creationUtcTime;
    private final UUID userId;
    private final List<UUID> orderItemsIds;
    private final EOrderStatus orderStatus;

    public OrderDTO(Order order){
        id = order.getId();
        creationUtcTime = order.getCreationUtcTime();
        userId = order.getUser().getId();
        orderItemsIds = order.getProducts().
                stream().
                map(OrderItem::getId).
                collect(Collectors.toList());
        orderStatus = order.getOrderStatus();
    }
}
