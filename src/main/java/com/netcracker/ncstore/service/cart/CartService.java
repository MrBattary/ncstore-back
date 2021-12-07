package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.data.OrderDTO;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.config.event.customevent.CartServiceSessionEndedEvent;
import com.netcracker.ncstore.exception.CartServiceCheckoutException;
import com.netcracker.ncstore.exception.CartServiceValidationException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.model.Cart;
import com.netcracker.ncstore.model.CartItem;
import com.netcracker.ncstore.repository.CartItemRepository;
import com.netcracker.ncstore.repository.CartRepository;
import com.netcracker.ncstore.service.order.IOrderService;
import com.netcracker.ncstore.service.product.IProductsService;
import com.netcracker.ncstore.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@SessionScope
@Slf4j
public class CartService implements ICartService {
    private final IProductsService productsService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IUserService userService;
    private final IOrderService orderService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private UUID userId;

    private final Map<UUID, Integer> cartMap;

    public CartService(final IProductsService productsService,
                       final CartRepository cartRepository,
                       final CartItemRepository cartItemRepository,
                       final IUserService userService,
                       final IOrderService orderService,
                       final ApplicationEventPublisher applicationEventPublisher) {

        this.productsService = productsService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.orderService = orderService;
        this.applicationEventPublisher = applicationEventPublisher;

        if (SecurityContextHolder.getContext() != null) {
            userId = userService.loadUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();

            Cart cartEntity = cartRepository.findById(userId).orElse(null);

            if (cartEntity != null) {
                cartMap = cartEntity.getCartItems().stream().collect(Collectors.toMap(CartItem::getProductId, CartItem::getCount));
            } else {
                cartMap = new HashMap<>();
            }
        } else {
            userId = null;
            cartMap = new HashMap<>();
        }
    }

    @PreDestroy
    public void saveCart(){
        if (userId != null && cartMap!=null) {
            CartServiceSessionEndedEvent event = new CartServiceSessionEndedEvent(this, new HashMap(cartMap), userId);
            applicationEventPublisher.publishEvent(event);
        }
    }

    @Override
    public Map<UUID, Integer> getCartItems() {
        return new HashMap<>(cartMap);
    }

    @Override
    public void addOrUpdateProduct(UUID productId, Integer count) {
        if (count < 1) {
            throw new CartServiceValidationException("Product count must be non-zero positive integer.");
        }
        if (!productsService.doesProductExist(productId)) {
            throw new CartServiceValidationException("Product with provided UUID does not exist.");
        }

        cartMap.put(productId, count);
    }

    @Override
    public Integer deleteProduct(UUID productId) {
        Integer count = cartMap.remove(productId);
        return count == null ? 0 : count;
    }

    @Override
    public OrderInfoResponse checkout(Locale locale) throws CartServiceCheckoutException {
        if (userId == null) {
            throw new CartServiceCheckoutException("Cant checkout anonymous user");
        } else if (CollectionUtils.isEmpty(cartMap)) {
            throw new CartServiceCheckoutException("Cant checkout empty cart");
        } else {
            try {
                OrderDTO orderDTO = orderService.checkoutUserCart(new CartCheckoutDetails(new HashMap<>(cartMap), userId, locale));
                cartMap.clear();
                return orderService.getOrderInfoResponse(orderDTO.getId());

            } catch (OrderServiceOrderCreationException e) {
                log.error(e.getMessage());
                throw new CartServiceCheckoutException("Can not checkout due to order creation problem.", e);
            }
        }
    }
}
