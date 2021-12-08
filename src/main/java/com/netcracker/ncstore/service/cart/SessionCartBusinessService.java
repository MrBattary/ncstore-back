package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.config.event.customevent.CartServiceSessionEndedEvent;
import com.netcracker.ncstore.dto.CartItemDTO;
import com.netcracker.ncstore.dto.CartPutDTO;
import com.netcracker.ncstore.exception.CartServiceValidationException;
import com.netcracker.ncstore.model.Cart;
import com.netcracker.ncstore.model.CartItem;
import com.netcracker.ncstore.repository.CartRepository;
import com.netcracker.ncstore.service.cart.interfaces.ICartBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@SessionScope
@Slf4j
public class SessionCartBusinessService implements ICartBusinessService {
    private String userEmail;

    private final Map<UUID, Integer> cartMap;
    private final ApplicationEventPublisher applicationEventPublisher;

    public SessionCartBusinessService(final CartRepository cartRepository,
                                      final ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;

        userEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!userEmail.equals("ANONYMOUS")) {
            Cart cartEntity = cartRepository.findByUserEmail(userEmail).orElse(null);

            if (cartEntity != null) {
                log.info("Loaded cart from database for user with email " + userEmail);
                cartMap = cartEntity.getCartItems().stream().collect(Collectors.toMap(CartItem::getProductId, CartItem::getCount));
            } else {
                log.info("Created new cart for user with email " + userEmail);
                cartMap = new HashMap<>();
            }
        } else {
            log.info("Created cart bean for anonymous user");
            userEmail = null;
            cartMap = new HashMap<>();
        }
    }

    @Override
    public List<CartItemDTO> getCartItems() {
        return cartMap.
                entrySet().
                stream().
                map(e -> new CartItemDTO(
                                e.getKey(),
                                e.getValue()
                        )
                ).
                collect(Collectors.toList());
    }

    @Override
    public CartItemDTO addOrUpdateProductInCart(CartPutDTO putDTO) {
        if (putDTO.getCountOfProduct() < 1) {
            throw new CartServiceValidationException("Product count must be non-zero positive integer.");
        }
        cartMap.put(putDTO.getProductId(), putDTO.getCountOfProduct());

        return new CartItemDTO(
                putDTO.getProductId(),
                cartMap.get(putDTO.getProductId())
        );
    }

    @Override
    public CartItemDTO deleteProductFromCart(UUID productId) {
        Integer deletedProductCount = cartMap.remove(productId);

        return new CartItemDTO(
                productId,
                deletedProductCount == null ? 0 : deletedProductCount
        );
    }

    @Override
    public String getCartUserEmail() {
        return userEmail;
    }

    @Override
    public void setCartUserEmail(String email) {
        this.userEmail = email;
    }

    @PreDestroy
    public void saveCart(){
        if (userEmail != null && cartMap!=null) {
            CartServiceSessionEndedEvent event = new CartServiceSessionEndedEvent(this, new HashMap(cartMap), userEmail);
            applicationEventPublisher.publishEvent(event);
        }
    }
}
