package com.netcracker.ncstore.service.web.cart;

import com.netcracker.ncstore.dto.request.CartCheckoutRequest;
import com.netcracker.ncstore.dto.request.CartDeleteRequest;
import com.netcracker.ncstore.dto.request.CartGetRequest;
import com.netcracker.ncstore.dto.request.CartPutRequest;
import com.netcracker.ncstore.dto.response.CartItemResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;

import java.util.List;

/**
 * General Web interface for cart service, which will be communicating with controller.
 */
public interface ICartWebService {
    /**
     * Gets all items from cart
     *
     * @param request request
     * @return List of items
     */
    List<CartItemResponse> getCartItems(final CartGetRequest request);

    /**
     * Adds product to cart or update its count if it was already there.
     *
     * @param request request
     * @return added/updated item
     */
    CartItemResponse putCartItem(final CartPutRequest request);

    /**
     * Deletes specified product in cart.
     * Returns null if there was no such product in cart,
     * or repose containing info about product in cart before deletion
     *
     * @param request DTO containing needed info
     * @return null if was deleted, or CartItemResponse containing info about deleted product
     */
    CartItemResponse deleteCartItem(final CartDeleteRequest request);

    /**
     * Preforms order cart checkout
     *
     * @param request request
     * @return
     */
    OrderInfoResponse checkoutCartForUser(final CartCheckoutRequest request);
}
