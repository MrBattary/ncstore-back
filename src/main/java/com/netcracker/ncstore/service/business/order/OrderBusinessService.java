package com.netcracker.ncstore.service.business.order;

import com.netcracker.ncstore.dto.ActualProductPrice;
import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.OrderGetDTO;
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
import com.netcracker.ncstore.repository.OrderItemRepository;
import com.netcracker.ncstore.repository.OrderRepository;
import com.netcracker.ncstore.service.data.order.IOrderDataService;
import com.netcracker.ncstore.service.general.payment.IPaymentService;
import com.netcracker.ncstore.service.data.price.IPricesDataService;
import com.netcracker.ncstore.service.general.priceconverter.IPriceConversionService;
import com.netcracker.ncstore.service.data.product.IProductDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final IProductDataService productDataService;
    private final IPricesDataService pricesDataService;
    private final IPriceConversionService priceConversionService;
    private final IPaymentService paymentService;
    private final IOrderDataService orderDataService;

    @Value("${store.sales_percentage}")
    private double ownerPercent;

    public OrderBusinessService(final OrderRepository orderRepository,
                                final OrderItemRepository orderItemRepository,
                                final IProductDataService productDataService,
                                final IPricesDataService pricesDataService,
                                final IPriceConversionService priceConversionService,
                                final IPaymentService paymentService,
                                final IOrderDataService orderDataService) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productDataService = productDataService;
        this.pricesDataService = pricesDataService;
        this.priceConversionService = priceConversionService;
        this.paymentService = paymentService;
        this.orderDataService = orderDataService;
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
                            productDataService.getProductById(e.getKey())
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
            Order order = orderDataService.getSpecificOrderForUser(
                    new OrderGetDTO(
                            orderPayDTO.getOrderId(),
                            orderPayDTO.getEmail()
                    )
            );

            double UCSum = 0;

            for (OrderItem item : order.getProducts()) {
                ActualProductPrice productPrice = pricesDataService.getActualPriceForProductInRegion(
                        new ProductLocaleDTO(
                                item.getProduct().getId(),
                                orderPayDTO.getRegion()
                        )
                );
                ActualProductPriceConvertedForRegionDTO convertedPrice =
                        priceConversionService.getActualConvertedPriceForProductInRegion(productPrice);

                addMoneyToSupplierForProduct(
                        item.getProduct().getId(),
                        productPrice.getProductPrice().getPriceWithDiscount()
                );

                UCSum += productPrice.getProductPrice().getPriceWithDiscount();

                item.setItemStatus(EOrderItemStatus.PAID);
                item.setLocalizedPrice(convertedPrice.getRealPrice());
                item.setPriceUc(productPrice.getProductPrice().getPriceWithDiscount());
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
                                priceInRealMoney.getLocale(),
                                order.getUser().getId()
                        )
                );
            } catch (PaymentServiceException e) {
                throw new OrderServiceValidationException("Can not checkout because card payment was not successful. " + e.getMessage(), e);
            }
        }

        return transactionId;
    }

    private void addMoneyToSupplierForProduct(final UUID productId, final double amount) {
        Product product = productDataService.getProductById(productId);
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
                if (!productDataService.checkIfProductIsOnSale(e.getKey())) {
                    throw new OrderServiceValidationException("Product with UUID " + e.getKey() + " is not for sale. ");
                }
            } catch (ProductServiceNotFoundException exception) {
                throw new OrderServiceValidationException(exception.getMessage());
            }
        }
    }
}
