package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.exception.CartServiceValidationException;
import com.netcracker.ncstore.model.Cart;
import com.netcracker.ncstore.model.CartItem;
import com.netcracker.ncstore.repository.CartItemRepository;
import com.netcracker.ncstore.repository.CartRepository;
import com.netcracker.ncstore.service.product.IProductsService;
import com.netcracker.ncstore.service.user.IUserService;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.SessionScope;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@SessionScope
public class CartService implements ICartService {
    private final IProductsService productsService;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IUserService userService;
    private UUID userId;

    private final Map<UUID, Integer> cartMap;

    public CartService(final IProductsService productsService,
                       final CartRepository cartRepository,
                       final CartItemRepository cartItemRepository,
                       final IUserService userService) {
        this.productsService = productsService;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;

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

    @Transactional
    @EventListener(SessionDestroyedEvent.class)
    public void saveCart() {
        if (userId != null && cartMap.size() != 0) {
            Cart savedCart;

            if (!cartRepository.existsById(userId)) {
                Cart cartToSave = new Cart(
                        null,
                        Instant.now(),
                        userService.loadUserEntityById(userId),
                        null);

                savedCart = cartRepository.save(cartToSave);


            } else {
                savedCart = cartRepository.getById(userId);
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
}
