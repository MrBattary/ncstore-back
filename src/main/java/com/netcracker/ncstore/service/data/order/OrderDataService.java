package com.netcracker.ncstore.service.data.order;

import com.netcracker.ncstore.dto.OrderGetDTO;
import com.netcracker.ncstore.dto.OrderGetPageDTO;
import com.netcracker.ncstore.exception.OrderServiceNotFoundException;
import com.netcracker.ncstore.exception.OrderServicePermissionException;
import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrderDataService implements IOrderDataService {
    private final OrderRepository orderRepository;

    public OrderDataService(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Page<Order> getOrdersForUserWithPagination(final OrderGetPageDTO orderGetPageDTO) {
        return orderRepository.findByUserEmail(orderGetPageDTO.getEmail(), orderGetPageDTO.getPageable());
    }

    @Override
    public Order getSpecificOrderForUser(OrderGetDTO orderGetDTO) throws OrderServiceNotFoundException, OrderServicePermissionException {
        UUID orderId = orderGetDTO.getOrderId();
        Order order = orderRepository.
                findById(orderId).orElseThrow(
                        () -> new OrderServiceNotFoundException("Requested order does not exist. ")
                );

        if (order.getUser().getEmail().equals(orderGetDTO.getEmail())) {
            return order;
        } else {
            throw new OrderServicePermissionException("Requested order does not belong to provided user. ");
        }
    }
}
