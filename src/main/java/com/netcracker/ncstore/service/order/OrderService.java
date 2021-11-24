package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.data.OrderDTO;
import com.netcracker.ncstore.repository.OrderRepository;
import com.netcracker.ncstore.service.discount.IDiscountsService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.product.IProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderService implements IOrderService{
    private final OrderRepository orderRepository;
    private final IProductsService productsService;
    private final IPricesService pricesService;
    private final IDiscountsService discountsService;


    public OrderService(final OrderRepository orderRepository,
                        final IProductsService productsService,
                        final IPricesService pricesService,
                        final IDiscountsService discountsService) {
        this.orderRepository = orderRepository;
        this.productsService = productsService;
        this.pricesService = pricesService;
        this.discountsService = discountsService;
    }


    @Override
    public OrderDTO purchaseProductForUser(CartCheckoutDetails details) {
        return null;
    }
}
