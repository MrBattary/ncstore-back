package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.exception.CartServiceValidationException;
import com.netcracker.ncstore.service.product.IProductsService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@SessionScope
public class CartService implements ICartService {
    private final IProductsService productsService;

    private final Map<UUID, Integer> cart;

    public CartService(final IProductsService productsService) {
        this.productsService = productsService;
        cart = new HashMap<>();
    }


    @Override
    public Map<UUID, Integer> getCartItems() {
        return new HashMap<>(cart);
    }

    @Override
    public void addOrUpdateProduct(UUID productId, Integer count) {
        if (count < 1) {
            throw new CartServiceValidationException("Product count must be non-zero positive integer.");
        }
        if (!productsService.doesProductExist(productId)) {
            throw new CartServiceValidationException("Product with provided UUID does not exist.");
        }

        cart.put(productId, count);
    }

    @Override
    public boolean deleteProduct(UUID productId) {
        return cart.remove(productId) != null;
    }
}
