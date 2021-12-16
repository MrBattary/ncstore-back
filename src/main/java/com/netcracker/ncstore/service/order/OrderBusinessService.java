package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.OrderGetDTO;
import com.netcracker.ncstore.dto.OrderGetPageDTO;
import com.netcracker.ncstore.dto.OrderPayDTO;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.create.OrderCreateDTO;
import com.netcracker.ncstore.exception.OrderServiceNotFoundException;
import com.netcracker.ncstore.exception.OrderServiceOrderCompletionException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.exception.OrderServiceOrderPaymentException;
import com.netcracker.ncstore.exception.OrderServicePermissionException;
import com.netcracker.ncstore.exception.OrderServiceValidationException;
import com.netcracker.ncstore.exception.PaymentServiceException;
import com.netcracker.ncstore.exception.ProductServiceNotFoundException;
import com.netcracker.ncstore.model.Order;
import com.netcracker.ncstore.model.OrderItem;
import com.netcracker.ncstore.model.Product;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.model.enumerations.EOrderItemStatus;
import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import com.netcracker.ncstore.model.enumerations.EProductStatus;
import com.netcracker.ncstore.repository.OrderItemRepository;
import com.netcracker.ncstore.repository.OrderRepository;
import com.netcracker.ncstore.service.order.interfaces.IOrderBusinessService;
import com.netcracker.ncstore.service.payment.IPaymentService;
import com.netcracker.ncstore.service.price.IPricesService;
import com.netcracker.ncstore.service.priceconverter.IPriceConversionService;
import com.netcracker.ncstore.service.product.IProductsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class OrderBusinessService implements IOrderBusinessService {
    @Value("${store.sales_percentage}")
    private double ownerPercent;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final IProductsService productsService;
    private final IPricesService pricesService;
    private final IPriceConversionService priceConversionService;
    private final IPaymentService paymentService;

    public OrderBusinessService(final OrderRepository orderRepository,
                                final OrderItemRepository orderItemRepository,
                                final IProductsService productsService,
                                final IPricesService pricesService,
                                final IPriceConversionService priceConversionService,
                                final IPaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productsService = productsService;
        this.pricesService = pricesService;
        this.priceConversionService = priceConversionService;
        this.paymentService = paymentService;
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

    @Override
    @Transactional
    public Order createNewUnpaidOrder(final OrderCreateDTO orderCreateDTO) throws OrderServiceOrderCreationException {
        try {
            User customer = orderCreateDTO.getCustomer();
            log.info("Started ordering for customer with UUID " + customer.getId());

            validateOrderCreateProducts(orderCreateDTO);

            Order order = new Order(Instant.now(), customer, EOrderStatus.UNPAID);

            List<OrderItem> orderItemList = new ArrayList<>();

            for (Map.Entry<UUID, Integer> e : orderCreateDTO.getProductsToBuyWithCount().entrySet()) {
                for (int i = 0; i < e.getValue(); i++) {
                    OrderItem orderItem = new OrderItem(
                            order,
                            productsService.loadProductEntityById(e.getKey())
                    );
                    orderItemList.add(orderItem);
                }
            }

            order.setProducts(orderItemList);
            order = orderRepository.save(order);
            orderItemRepository.saveAll(orderItemList);
            orderRepository.flush();
            orderItemRepository.flush();

            log.info("Successfully created new unpaid order for customer with UUID " + customer.getId());

            return order;
        } catch (OrderServiceValidationException e) {
            log.error(e.getMessage());
            throw new OrderServiceOrderCreationException("Can not create order. " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public Order payExistingOrder(final OrderPayDTO orderPayDTO) throws OrderServiceOrderPaymentException {
        log.info("Started payment for order with UUID " + orderPayDTO.getOrderId());

        try {
            Order order = getSpecificOrderForUser(
                    new OrderGetDTO(
                            orderPayDTO.getOrderId(),
                            orderPayDTO.getEmail()
                    )
            );

            double UCSum = 0;

            for (OrderItem item : order.getProducts()) {
                ActualProductPriceInRegionDTO actualPrice = pricesService.getActualPriceForProductInRegion(
                        new ProductLocaleDTO(
                                item.getProduct().getId(),
                                orderPayDTO.getRegion()
                        )
                );
                ActualProductPriceConvertedForRegionDTO convertedPrice =
                        priceConversionService.convertActualUCPriceForRealPrice(actualPrice);

                addMoneyToSupplierForProduct(item.getProduct().getId(), actualPrice.getRealPrice());
                UCSum += actualPrice.getRealPrice();

                item.setItemStatus(EOrderItemStatus.PAID);
                item.setPrice(convertedPrice.getRealPrice());
                item.setPriceLocale(convertedPrice.getRegion());
            }

            String transactionId = proceedPayment(orderPayDTO, UCSum);

            order.setOrderStatus(EOrderStatus.PAID);
            order.setTransactionId(transactionId);

            log.info("Successfully paid order with UUID " + orderPayDTO.getOrderId());
            return order;

        } catch (OrderServiceNotFoundException | OrderServiceValidationException | OrderServicePermissionException e) {
            log.error("Can not pay the order with UUID " + orderPayDTO.getOrderId() + ". " + e.getMessage());
            throw new OrderServiceOrderPaymentException("Can not pay the order with UUID " + orderPayDTO.getOrderId() + ". " + e.getMessage(), e);
        }

    }

    @Override
    @Transactional
    public Order completePaidOrder(UUID orderId) throws OrderServiceOrderCompletionException {
        Order order = orderRepository.
                findById(orderId).
                orElseThrow(
                        () -> new OrderServiceOrderCompletionException("Order with UUID " + orderId + " not found. ")
                );

        if (!order.getOrderStatus().equals(EOrderStatus.PAID)) {
            throw new OrderServiceOrderCompletionException("Order with UUID " + orderId + " not paid so it can not be completed. ");
        }

        for (OrderItem item : order.getProducts()) {
            item.setItemStatus(EOrderItemStatus.COMPLETED);
            //TODO not random keys
            item.setLicenseKey(UUID.randomUUID().toString());
        }

        order.setOrderStatus(EOrderStatus.COMPLETED);

        return order;
    }

    private String proceedPayment(final OrderPayDTO orderPayDTO, final double sum) throws OrderServiceValidationException {
        if (!orderPayDTO.isUseBalance() && orderPayDTO.getNonce() == null) {
            throw new OrderServiceValidationException("Can not checkout because card data is not valid. ");
        }

        Order order = orderRepository.getById(orderPayDTO.getOrderId());

        String transactionId = null;

        if (orderPayDTO.isUseBalance()) {
            if (order.getUser().getBalance() < sum) {
                throw new OrderServiceValidationException("There is not enough money on balance to checkout. ");
            } else {
                order.getUser().setBalance(order.getUser().getBalance() - sum);
            }
        } else {
            try {
                ConvertedPriceWithCurrencySymbolDTO priceInRealMoney =
                        priceConversionService.convertUCPriceToRealPriceWithSymbol(sum, orderPayDTO.getRegion());

                transactionId = paymentService.proceedPaymentInRealMoney(new PaymentProceedDTO(
                                BigDecimal.valueOf(priceInRealMoney.getPrice()),
                                orderPayDTO.getNonce(),
                                priceInRealMoney.getLocale()
                        )
                );
            } catch (PaymentServiceException e) {
                throw new OrderServiceValidationException("Can not checkout because card payment was not successful. " + e.getMessage(), e);
            }
        }

        return transactionId;
    }

    private void addMoneyToSupplierForProduct(final UUID productId, final double amount) {
        Product product = productsService.loadProductEntityById(productId);
        User supplier = product.getSupplier();
        double supplierMoney = amount * (1 - ownerPercent);
        double shopOwnerMoney = amount * ownerPercent;
        //bad log because it no money will be added on failed transaction. rewrite in future if needed
        //log.info("Added " + supplierMoney + " UC to supplier with UUID " + supplier.getId() + " for product with UUID " + product.getId() + ". Shop revenue is " + shopOwnerMoney + " UC");
        supplier.setBalance(supplier.getBalance() + supplierMoney);
        //TODO add money to admin
    }

    private void validateOrderCreateProducts(final OrderCreateDTO orderCreateDTO) throws OrderServiceOrderCreationException {
        if (CollectionUtils.isEmpty(orderCreateDTO.getProductsToBuyWithCount())) {
            throw new OrderServiceValidationException("Order list is empty. ");
        }
        for (Map.Entry<UUID, Integer> e : orderCreateDTO.getProductsToBuyWithCount().entrySet()) {
            try {
                Product product = productsService.loadProductEntityById(e.getKey());
                if (!product.getProductStatus().equals(EProductStatus.IN_STOCK)) {
                    throw new OrderServiceValidationException("Product with UUID " + e.getKey() + " has status " + product.getProductStatus().toString() + ". ");
                }
            } catch (ProductServiceNotFoundException exception) {
                throw new OrderServiceValidationException(exception.getMessage());
            }
        }
    }
}
