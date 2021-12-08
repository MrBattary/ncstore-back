package com.netcracker.ncstore.service.order.interfaces;

import com.netcracker.ncstore.dto.OrderGetDTO;
import com.netcracker.ncstore.dto.OrderGetPageDTO;
import com.netcracker.ncstore.dto.create.OrderCreateDTO;
import com.netcracker.ncstore.exception.OrderServiceNotFoundException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.exception.OrderServicePermissionException;
import com.netcracker.ncstore.model.Order;
import org.springframework.data.domain.Page;

public interface IOrderBusinessService {
    Page<Order> getOrdersForUserWithPagination(OrderGetPageDTO orderGetPageDTO);
    Order getSpecificOrderForUser(OrderGetDTO orderGetDTO) throws OrderServiceNotFoundException, OrderServicePermissionException;
    Order createNewOrder(OrderCreateDTO orderCreateDTO) throws OrderServiceOrderCreationException;
}
