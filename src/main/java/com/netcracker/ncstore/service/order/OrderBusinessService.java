package com.netcracker.ncstore.service.order;

import com.netcracker.ncstore.dto.ActualProductPriceConvertedForRegionDTO;
import com.netcracker.ncstore.dto.ActualProductPriceInRegionDTO;
import com.netcracker.ncstore.dto.ConvertedPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.OrderGetDTO;
import com.netcracker.ncstore.dto.OrderGetPageDTO;
import com.netcracker.ncstore.dto.PaymentProceedDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.create.OrderCreateDTO;
import com.netcracker.ncstore.exception.OrderServiceNotFoundException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.exception.OrderServicePermissionException;
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
import java.util.HashMap;
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

        if (order.getUser().getEmail().equals(orderGetDTO.getEmail())) {
            return order;
        } else {
            throw new OrderServicePermissionException("Requested order does not belong to provided user");
        }
    }

    @Override
    @Transactional
    public Order createNewOrder(OrderCreateDTO orderCreateDTO) throws OrderServiceOrderCreationException {
        String transactionId = null;

        try {
            User customer = orderCreateDTO.getCustomer();
            log.info("Started ordering for customer with UUID " + customer.getId());

            validateOrderCreateProducts(orderCreateDTO);

            if (!orderCreateDTO.isUseBalance() && orderCreateDTO.getNonce() == null) {
                throw new OrderServiceValidationException("Can not checkout because card data is not valid. ");
            }

            Map<ActualProductPriceInRegionDTO, Integer> actualProductPriceAndCountMap = calculateRealPricesForProducts(orderCreateDTO);

            double sum = actualProductPriceAndCountMap.entrySet().
                    stream().
                    mapToDouble(e -> e.getKey().getRealPrice() * e.getValue()).
                    sum();

            if (orderCreateDTO.isUseBalance()) {
                if (customer.getBalance() < sum) {
                    throw new OrderServiceValidationException("There is not enough money ob balance to checkout. ");
                }
            } else {
                try {
                    ConvertedPriceWithCurrencySymbolDTO priceInRealMoney =
                            priceConversionService.convertUCPriceToRealPriceWithSymbol(sum, orderCreateDTO.getRegion());

                    transactionId = paymentService.proceedPaymentInRealMoney(new PaymentProceedDTO(
                                    BigDecimal.valueOf(priceInRealMoney.getPrice()),
                                    orderCreateDTO.getNonce(),
                                    priceInRealMoney.getLocale()
                            )
                    );
                } catch (PaymentServiceException e) {
                    throw new OrderServiceValidationException("Can not checkout because card payment was not successful. " + e.getMessage(), e);
                }
            }

            Order order = new Order(Instant.now(), customer, EOrderStatus.COMPLETED);

            List<OrderItem> orderItemList = new ArrayList<>();

            for (Map.Entry<ActualProductPriceInRegionDTO, Integer> e : actualProductPriceAndCountMap.entrySet()) {
                ActualProductPriceInRegionDTO actualPrice = e.getKey();
                ActualProductPriceConvertedForRegionDTO convertedPrice =
                        priceConversionService.convertActualUCPriceForRealPrice(actualPrice);

                for (int i = 0; i < e.getValue(); i++) {
                    OrderItem orderItem = new OrderItem(
                            convertedPrice.getRealPrice(),
                            convertedPrice.getRegion(),
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
            customer.setBalance(customer.getBalance() - sum);
            order = orderRepository.save(order);
            orderItemRepository.saveAll(orderItemList);
            orderRepository.flush();
            orderItemRepository.flush();

            log.info("Successfully completed ordering for customer with UUID " + customer.getId() + " with final sum of " + sum + " UC");

            return order;
        } catch (OrderServiceValidationException e) {
            if (transactionId != null) {
                //TODO cancel transaction
            }
            log.error(e.getMessage());
            throw new OrderServiceOrderCreationException("Can not create order. " + e.getMessage(), e);
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

    private void validateOrderCreateProducts(OrderCreateDTO orderCreateDTO) throws OrderServiceOrderCreationException {
        if (CollectionUtils.isEmpty(orderCreateDTO.getProductsToBuyWithCount())) {
            throw new OrderServiceValidationException("Order list is empty. ");
        }
        for (Map.Entry<UUID, Integer> e : orderCreateDTO.getProductsToBuyWithCount().entrySet()) {
            Product product = productsService.loadProductEntityById(e.getKey());
            if (product == null) {
                throw new OrderServiceValidationException("Cant create order because product with UUID " + e.getKey() + " does not exist. ");
            }
            if (!product.getProductStatus().equals(EProductStatus.IN_STOCK)) {
                throw new OrderServiceValidationException("Cant create order because product with UUID " + e.getKey() + " has status " + product.getProductStatus().toString() + ". ");
            }
        }
    }

    private Map<ActualProductPriceInRegionDTO, Integer> calculateRealPricesForProducts(OrderCreateDTO orderCreateDTO) {
        Map<ActualProductPriceInRegionDTO, Integer> productIdPriceMap = new HashMap<>();
        for (Map.Entry<UUID, Integer> e : orderCreateDTO.getProductsToBuyWithCount().entrySet()) {
            ActualProductPriceInRegionDTO actualPrice = pricesService.getActualPriceForProductInRegion(
                    new ProductLocaleDTO(e.getKey(),
                            orderCreateDTO.getRegion())
            );

            productIdPriceMap.put(actualPrice, e.getValue());
        }
        return productIdPriceMap;
    }
}
