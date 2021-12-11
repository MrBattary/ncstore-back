package com.netcracker.ncstore.service.order.interfaces;

import com.netcracker.ncstore.dto.OrderGetDTO;
import com.netcracker.ncstore.dto.OrderGetPageDTO;
import com.netcracker.ncstore.dto.create.OrderCreateDTO;
import com.netcracker.ncstore.exception.OrderServiceNotFoundException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.exception.OrderServicePermissionException;
import com.netcracker.ncstore.model.Order;
import org.springframework.data.domain.Page;

/**
 * Interface for all services that contain business logic related to order
 */
public interface IOrderBusinessService {
    /**
     * Returns request Page of orders for user
     *
     * @param orderGetPageDTO DTO containing info about user and page
     * @return Page<Order>
     */
    Page<Order> getOrdersForUserWithPagination(final OrderGetPageDTO orderGetPageDTO);

    /**
     * Returns requested order only if it belongs to user
     *
     * @param orderGetDTO DTO containing order ID and user data
     * @return Order for provided ID if it belongs to user
     * @throws OrderServiceNotFoundException   when order with provided ID does not exist
     * @throws OrderServicePermissionException when user do not own requested order
     */
    Order getSpecificOrderForUser(final OrderGetDTO orderGetDTO) throws OrderServiceNotFoundException, OrderServicePermissionException;

    /**
     * Creates new Order with provided details
     *
     * @param orderCreateDTO DTO containing info for creating new order
     * @return created Order
     * @throws OrderServiceOrderCreationException if Order can not be created for provided data
     */
    Order createNewOrder(final OrderCreateDTO orderCreateDTO) throws OrderServiceOrderCreationException;
}
