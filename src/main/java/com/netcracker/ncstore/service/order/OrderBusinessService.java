package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.OrderGetDTO;
import com.netcracker.ncstore.dto.OrderGetPageDTO;
import com.netcracker.ncstore.dto.OrderItemGetDTO;
import com.netcracker.ncstore.exception.OrderServiceNotFoundException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.exception.OrderServicePermissionException;
import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.model.OrderItem;
import com.netcracker.ncstore.repository.OrderItemRepository;
import com.netcracker.ncstore.repository.OrderRepository;
import com.netcracker.ncstore.service.order.interfaces.IOrderBusinessService;
import com.netcracker.ncstore.service.user.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OrderBusinessService implements IOrderBusinessService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final IUserService userService;

    public OrderBusinessService(final OrderRepository orderRepository,
                                final OrderItemRepository orderItemRepository,
                                final IUserService userService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
    }


    @Override
    public Page<Order> getOrdersForUserWithPagination(OrderGetPageDTO orderGetPageDTO) {
        return orderRepository.findByUserEmail(orderGetPageDTO.getEmail(), orderGetPageDTO.getPageable());
    }

    @Override
    public Order getSpecificOrderForUser(OrderGetDTO orderGetDTO) {
        UUID orderId = orderGetDTO.getOrderId();
        Order order = orderRepository.
                findById(orderId).orElseThrow(
                        () -> new OrderServiceNotFoundException("Requested order does not exist")
                );

        if(order.getUser().getEmail().equals(orderGetDTO.getEmail())) {
            return order;
        }else{
            throw new OrderServicePermissionException("Requested order does not belong to provided user");
        }
    }

    @Override
    public Order checkoutUserCart(CartCheckoutDetails details) throws OrderServiceOrderCreationException {
        return null;
    }
}
