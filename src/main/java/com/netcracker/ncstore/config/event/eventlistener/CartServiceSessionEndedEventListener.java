package com.netcracker.ncstore.config.event.eventlistener;

import com.netcracker.ncstore.config.event.customevent.CartServiceSessionEndedEvent;
import com.netcracker.ncstore.model.Cart;
import com.netcracker.ncstore.model.CartItem;
import com.netcracker.ncstore.model.User;
import com.netcracker.ncstore.repository.CartItemRepository;
import com.netcracker.ncstore.repository.CartRepository;
import com.netcracker.ncstore.service.user.interfaces.IUserDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CartServiceSessionEndedEventListener implements ApplicationListener<CartServiceSessionEndedEvent> {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final IUserDataService userDataService;

    public CartServiceSessionEndedEventListener(final CartRepository cartRepository,
                                                final CartItemRepository cartItemRepository,
                                                final IUserDataService userDataService ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userDataService = userDataService;
    }


    @Override
    @Transactional
    public void onApplicationEvent(CartServiceSessionEndedEvent event) {
        Cart savedCart;

        log.info("Session for user " + event.getUserEmail() + " ended. Saving cart to database.");

        User user = userDataService.getUserByEmail(event.getUserEmail());

        if (!cartRepository.existsById(user.getId())) {
            Cart cartToSave = new Cart(
                    Instant.now(),
                    userDataService.getUserById(user.getId())
            );

            savedCart = cartRepository.save(cartToSave);


        } else {
            savedCart = cartRepository.getById(user.getId());
            savedCart.setCreationTime(Instant.now());
            cartItemRepository.deleteByCartId(user.getId());
        }

        List<CartItem> items = event.getItemsToSafe().entrySet().
                stream().
                map(e -> new CartItem(e.getKey(), e.getValue(), savedCart)).
                collect(Collectors.toList());

        cartItemRepository.saveAll(items);
    }
}
