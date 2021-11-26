package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.data.OrderDTO;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.dto.response.OrderItemInfoResponse;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.model.OrderItem;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EOrderItemStatus;
import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.repository.OrderItemRepository;
import com.netcracker.ncstore.repository.OrderRepository;
import com.netcracker.ncstore.service.discount.IDiscountsService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.priceconverter.IPriceConversionService;
import com.netcracker.ncstore.service.product.IProductsService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final IProductsService productsService;
    private final IPricesService pricesService;
    private final IDiscountsService discountsService;
    private final IUserService userService;
    private final IPriceConversionService priceConversionService;
    private final OrderItemRepository orderItemRepository;


    public OrderService(final OrderRepository orderRepository,
                        final IProductsService productsService,
                        final IPricesService pricesService,
                        final IDiscountsService discountsService,
                        final IUserService userService,
                        final IPriceConversionService priceConversionService,
                        final OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.productsService = productsService;
        this.pricesService = pricesService;
        this.discountsService = discountsService;
        this.userService = userService;
        this.priceConversionService = priceConversionService;
        this.orderItemRepository = orderItemRepository;
    }


    @Override
    @Transactional
    public OrderDTO checkoutUserCart(CartCheckoutDetails details) throws OrderServiceOrderCreationException {
        User user = userService.loadUserEntityById(details.getUserId());

        log.info("Started ordering for user with UUID " + user.getId());

        double sum = 0;
        Map<UUID, Double> productPricesMap = new HashMap<>();
        Map<UUID, Product> productsToBuyMap = new HashMap<>();

        for (Map.Entry<UUID, Integer> e : details.getProductsToBuyWithCount().entrySet()) {
            Product product = productsService.loadProductEntityById(e.getKey());
            if (product == null) {
                throw new OrderServiceOrderCreationException("User with UUID " + user.getId() + " cant checkout because product with UUID " + e.getKey() + " does not exist");
            }
            if (!product.getProductStatus().equals(EProductStatus.IN_STOCK)) {
                throw new OrderServiceOrderCreationException("User with UUID " + user.getId() + " cant checkout because product with UUID " + e.getKey() + " has status " + product.getProductStatus().toString());
            }
            productsToBuyMap.put(e.getKey(), product);

            ActualProductPriceInRegionDTO actualPrice = pricesService.getActualPriceForProductInRegion(
                    new ProductLocaleDTO(e.getKey(),
                            details.getRegion())
            );

            double realProductPrice =
                    actualPrice.getDiscountPrice() == null
                            ?
                            actualPrice.getNormalPrice()
                            :
                            actualPrice.getDiscountPrice();

            productPricesMap.put(e.getKey(), realProductPrice);

            sum += realProductPrice * e.getValue();

        }

        if (user.getBalance() < sum) {
            throw new OrderServiceOrderCreationException("User with UUID " + user.getId() + " dont have enough money to checkout");
        }

        Order order = new Order(null, Instant.now(), user, new ArrayList<>(), EOrderStatus.COMPLETED);

        List<OrderItem> orderItemList = new ArrayList<>();

        for (Map.Entry<UUID, Integer> e : details.getProductsToBuyWithCount().entrySet()) {
            Double price = productPricesMap.get(e.getKey());

            ConvertedPriceWithCurrencySymbolDTO convertedPrice =
                    priceConversionService.convertUCPriceToRealPriceWithSymbol(price, details.getRegion()
                    );

            for (int i = 0; i < e.getValue(); i++) {
                OrderItem orderItem = new OrderItem(
                        null,
                        convertedPrice.getPrice(),
                        convertedPrice.getLocale(),
                        UUID.randomUUID().toString(),
                        order,
                        productsToBuyMap.get(e.getKey()),
                        EOrderItemStatus.COMPLETED
                );

                orderItemList.add(orderItem);
            }
        }

        order.setProducts(orderItemList);
        user.setBalance(user.getBalance() - sum);
        order = orderRepository.save(order);
        orderItemRepository.saveAll(orderItemList);

        log.info("Successfully completed ordering for user with UUID " + user.getId() + " with final sum of " + sum + " UC");

        return new OrderDTO(order);
    }

    @Override
    public OrderInfoResponse getOrderInfoResponse(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) {
            return null;
        } else {
            List<OrderItemInfoResponse> orderItems = order.getProducts().
                    stream().map(e -> new OrderItemInfoResponse(
                            e.getId(),
                            e.getProduct().getName(),
                            e.getProduct().getSupplier().getId(),
                            e.getPrice(),
                            LocaleToCurrencyConverter.getCurrencySymbolByLocale(e.getPriceLocale()),
                            e.getLicenseKey(),
                            e.getItemStatus())).
                    collect(Collectors.toList());

            return new OrderInfoResponse(orderId, order.getCreationUtcTime(), order.getOrderStatus(), orderItems);
        }
    }
}
