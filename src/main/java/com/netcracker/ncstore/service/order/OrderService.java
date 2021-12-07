package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.data.OrderDTO;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.dto.response.OrderItemInfoResponse;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.exception.OrderServiceValidationException;
import com.netcracker.ncstore.exception.PaymentServiceException;
import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.model.OrderItem;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EOrderItemStatus;
import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.repository.OrderItemRepository;
import com.netcracker.ncstore.repository.OrderRepository;
import com.netcracker.ncstore.service.payment.IPaymentService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.priceconverter.IPriceConversionService;
import com.netcracker.ncstore.service.product.IProductsService;
import com.netcracker.ncstore.service.user.IUserService;
import com.netcracker.ncstore.util.converter.LocaleToCurrencyConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    @Value("${store.sales_percentage}")
    private double ownerPercent;

    private final OrderRepository orderRepository;
    private final IProductsService productsService;
    private final IPricesService pricesService;
    private final IUserService userService;
    private final IPaymentService paymentService;
    private final OrderItemRepository orderItemRepository;
    private final IPriceConversionService priceConversionService;


    public OrderService(final OrderRepository orderRepository,
                        final IProductsService productsService,
                        final IPricesService pricesService,
                        final IUserService userService,
                        final IPaymentService paymentService,
                        final OrderItemRepository orderItemRepository,
                        final IPriceConversionService priceConversionService) {

        this.orderRepository = orderRepository;
        this.productsService = productsService;
        this.pricesService = pricesService;
        this.userService = userService;
        this.paymentService = paymentService;
        this.orderItemRepository = orderItemRepository;
        this.priceConversionService = priceConversionService;
    }

    @Override
    @Transactional
    public OrderDTO checkoutUserCart(CartCheckoutDetails details) throws OrderServiceOrderCreationException {
        User user = userService.loadUserEntityById(details.getUserId());

        log.info("Started ordering for user with UUID " + user.getId());

        try {
            checkOrderRequestProduct(details);
        } catch (OrderServiceValidationException e) {
            log.error(e.getMessage());
            throw new OrderServiceOrderCreationException("Unable to checkout because provided products not exist or out of stock");
        }

        if (!details.isUseBalance() && details.getNonce() == null) {
            throw new OrderServiceOrderCreationException("Can not checkout user with UUID " + user.getId() + " because one preferred using card payment but has not provided nonce");
        }

        Map<ActualProductPriceInRegionDTO, Integer> actualProductPriceAndCountMap = calculateRealPricesForProducts(details);

        double sum = actualProductPriceAndCountMap.entrySet().
                stream().
                mapToDouble(e -> e.getKey().getRealPrice() * e.getValue()).
                sum();

        if (details.isUseBalance()) {
            if (user.getBalance() < sum) {
                throw new OrderServiceOrderCreationException("User with UUID " + user.getId() + " dont have enough money to checkout");
            }
        } else {
            try {
                ConvertedPriceWithCurrencySymbolDTO priceInRealMoney =
                        priceConversionService.convertUCPriceToRealPriceWithSymbol(sum, details.getRegion());

                paymentService.proceedPaymentInRealMoney(new PaymentProceedDTO(
                                BigDecimal.valueOf(priceInRealMoney.getPrice()),
                                details.getNonce(),
                                priceInRealMoney.getLocale()
                        )
                );
            } catch (PaymentServiceException e) {
                log.error(e.getMessage());
                throw new OrderServiceOrderCreationException("Can not checkout user with UUID " + user.getId() + " because card payment was not successful");
            }
        }

        Order order = new Order(null, Instant.now(), user, new ArrayList<>(), EOrderStatus.COMPLETED);

        List<OrderItem> orderItemList = new ArrayList<>();

        for (Map.Entry<ActualProductPriceInRegionDTO, Integer> e : actualProductPriceAndCountMap.entrySet()) {
            ActualProductPriceInRegionDTO actualPrice = e.getKey();
            ActualProductPriceConvertedForRegionDTO convertedPrice =
                    priceConversionService.convertActualUCPriceForRealPrice(actualPrice);

            for (int i = 0; i < e.getValue(); i++) {
                OrderItem orderItem = new OrderItem(
                        null,
                        convertedPrice.getRealPrice(),
                        details.getRegion(),
                        UUID.randomUUID().toString(),
                        order,
                        productsService.loadProductEntityById(actualPrice.getProductId()),
                        EOrderItemStatus.COMPLETED
                );
                addMoneyToSupplierForProduct(actualPrice.getProductId(), actualPrice.getRealPrice());
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
                            e.getProduct().getId(),
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

    private void addMoneyToSupplierForProduct(UUID productId, double amount) {
        Product product = productsService.loadProductEntityById(productId);
        User supplier = product.getSupplier();
        double supplierMoney = amount * (1 - ownerPercent);
        double shopOwnerMoney = amount * ownerPercent;
        log.info("Added " + supplierMoney + " UC to supplier with UUID " + supplier.getId() + " for product with UUID " + product.getId() + ". Shop revenue is " + shopOwnerMoney + " UC");
        supplier.setBalance(supplier.getBalance() + supplierMoney);
        //TODO add money to admin
    }

    private void checkOrderRequestProduct(CartCheckoutDetails details) throws OrderServiceOrderCreationException {
        for (Map.Entry<UUID, Integer> e : details.getProductsToBuyWithCount().entrySet()) {
            Product product = productsService.loadProductEntityById(e.getKey());
            if (product == null) {
                throw new OrderServiceValidationException("User with UUID " + details.getUserId() + " cant checkout because product with UUID " + e.getKey() + " does not exist");
            }
            if (!product.getProductStatus().equals(EProductStatus.IN_STOCK)) {
                throw new OrderServiceValidationException("User with UUID " + details.getUserId() + " cant checkout because product with UUID " + e.getKey() + " has status " + product.getProductStatus().toString());
            }
        }
    }

    private Map<ActualProductPriceInRegionDTO, Integer> calculateRealPricesForProducts(CartCheckoutDetails details) {
        Map<ActualProductPriceInRegionDTO, Integer> productIdPriceMap = new HashMap<>();
        for (Map.Entry<UUID, Integer> e : details.getProductsToBuyWithCount().entrySet()) {
            ActualProductPriceInRegionDTO actualPrice = pricesService.getActualPriceForProductInRegion(
                    new ProductLocaleDTO(e.getKey(),
                            details.getRegion())
            );

            productIdPriceMap.put(actualPrice, e.getValue());
        }
        return productIdPriceMap;
    }
}
