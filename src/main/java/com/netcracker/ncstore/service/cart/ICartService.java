package com.netcracker.ncstore.service.cart;

import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.exception.CartServiceCheckoutException;
import com.netcracker.ncstore.exception.CartServiceValidationException;

import java.util.Locale;
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
     * @param count     - count or how many products of that type we would like to place in cart
     * @throws CartServiceValidationException - when provided parameters are invalid
     */
    void addOrUpdateProduct(UUID productId, Integer count) throws CartServiceValidationException;

    /**
     * Deletes product with provided UUID from cart
     *
     * @param productId - UUID of product
     * @return Integer representing the count of deleted product. 0 if product was not in cart
     */
    Integer deleteProduct(UUID productId);

    /**
     * Checkouts user and creates order
     *
     * @param locale - locale in which prices will be calculated
     * @return dto containing information about order
     * @throws CartServiceCheckoutException when some problem occurred while checkout process
     */
    OrderInfoResponse checkout(Locale locale) throws CartServiceCheckoutException;
}
