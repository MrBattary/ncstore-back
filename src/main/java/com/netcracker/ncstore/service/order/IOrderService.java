package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.data.OrderDTO;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;

/**
 * Interface for service which works with Orders
 */
public interface IOrderService {
    /**
     * Creates order based on provided checkout details. Order is being paid immediately.
     *
     * @param details - details for order
     * @return dto containing info about created order
     * @throws OrderServiceOrderCreationException when order can not be created
     */
    OrderDTO checkoutUserCart(CartCheckoutDetails details) throws OrderServiceOrderCreationException;
}
