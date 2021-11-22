package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.exception.CartServiceValidationException;
import org.springframework.data.util.Pair;

import java.util.Map;
import java.util.UUID;

/**
 * Interface for shopping cart.
 */
public interface ICartService {
    /**
     * Returns map where key is UUID of product and value is count of products of that type in cart
     *
     * @return Map<UUID, Integer>
     */
    Map<UUID, Integer> getCartItems();

    /**
     * Adds or updates product with specified UUID to cart with provided count
     *
     * @param productId - UUID of product
     * @param count - count or how many products of that type we would like to place in cart
     * @throws CartServiceValidationException - when provided parameters are invalid
     */
    void addOrUpdateProduct(UUID productId, int count) throws CartServiceValidationException;

    /**
     * Deletes product with provided UUID from cart
     *
     * @param productId - UUID of product
     * @return true if product was deleted or false when was not (does not exist in cart structure)
     */
    boolean deleteProduct(UUID productId);
}
