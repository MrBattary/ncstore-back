package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.data.OrderDTO;

import java.util.Map;
import java.util.UUID;

/**
 * Interface for service which works with Orders
 */
public interface IOrderService {
    OrderDTO purchaseProductForUser(CartCheckoutDetails details);
}
