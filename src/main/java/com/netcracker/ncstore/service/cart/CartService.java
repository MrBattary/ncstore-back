package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.dto.CartCheckoutDTO;
import com.netcracker.ncstore.dto.CartCheckoutDetails;
import com.netcracker.ncstore.dto.data.OrderDTO;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.exception.CartServiceCheckoutException;
import com.netcracker.ncstore.exception.CartServiceValidationException;
import com.netcracker.ncstore.exception.OrderServiceOrderCreationException;
import com.netcracker.ncstore.model.Cart;
import com.netcracker.ncstore.model.CartItem;
import com.netcracker.ncstore.repository.CartItemRepository;
import com.netcracker.ncstore.repository.CartRepository;
import com.netcracker.ncstore.service.order.interfaces.IOrderService;
import com.netcracker.ncstore.service.product.IProductsService;
import com.netcracker.ncstore.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.annotation.SessionScope;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
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
    private UUID userId;

    private final Map<UUID, Integer> cartMap;

    public CartService(final IProductsService productsService,
                       final CartRepository cartRepository,
                       final CartItemRepository cartItemRepository,
                       final IUserService userService,
                       final IOrderService orderService) {

        this.productsService = productsService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
        this.orderService = orderService;

        if (SecurityContextHolder.getContext() != null) {
            userId = userService.loadUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();

            Cart cartEntity = cartRepository.findById(userId).orElse(null);

            if (cartEntity != null) {
                log.info("Loaded cart from database for user with UUID " + userId);
                cartMap = cartEntity.getCartItems().stream().collect(Collectors.toMap(CartItem::getProductId, CartItem::getCount));
            } else {
                log.info("Created new cart for user with UUID " + userId);
                cartMap = new HashMap<>();
            }
        } else {
            log.info("Created cart bean for anonymous user");
            userId = null;
            cartMap = new HashMap<>();
        }
    }

    @Transactional
    @EventListener(SessionDestroyedEvent.class)
    public void saveCart() {
        if (userId != null && cartMap != null) {
            Cart savedCart;
            log.info("Saving cart of user with UUID " + userId + " to database");

            if (!cartRepository.existsById(userId)) {
                Cart cartToSave = new Cart(
                        null,
                        Instant.now(),
                        userService.loadUserEntityById(userId),
                        null);

                savedCart = cartRepository.save(cartToSave);


            } else {
                savedCart = cartRepository.getById(userId);
                savedCart.setCreationTime(Instant.now());
                cartItemRepository.deleteByCartId(userId);
            }

            List<CartItem> items = cartMap.entrySet().
                    stream().
                    map(e -> new CartItem(null, e.getKey(), e.getValue(), savedCart)).
                    collect(Collectors.toList());

            cartItemRepository.saveAll(items);
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
    public OrderInfoResponse checkout(CartCheckoutDTO cartCheckoutDTO) throws CartServiceCheckoutException {
        if (userId == null) {
            throw new CartServiceCheckoutException("Cant checkout anonymous user");
        } else if (CollectionUtils.isEmpty(cartMap)) {
            throw new CartServiceCheckoutException("Cant checkout empty cart");
        } else {
            try {
                OrderDTO orderDTO = orderService.checkoutUserCart(new CartCheckoutDetails(
                                new HashMap<>(cartMap),
                                userId,
                                cartCheckoutDTO.getLocale(),
                                cartCheckoutDTO.isUseBalance(),
                                cartCheckoutDTO.getNonce()
                        )
                );
                cartMap.clear();
                return orderService.getOrderInfoResponse(orderDTO.getId());

            } catch (OrderServiceOrderCreationException e) {
                log.error(e.getMessage());
                throw new CartServiceCheckoutException("Can not checkout due to order creation problem.", e);
            }
        }
    }
}
