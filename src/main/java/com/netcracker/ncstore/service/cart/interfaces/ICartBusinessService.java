package com.netcracker.ncstore.service.cart.interfaces;

import com.netcracker.ncstore.dto.CartItemDTO;
import com.netcracker.ncstore.dto.CartPutDTO;
import com.netcracker.ncstore.exception.CartServiceValidationException;

import java.util.List;
import java.util.UUID;

/**
 * Interface for all business services related to cart.
 * Such services should contain business logic related to cart.
 */
public interface ICartBusinessService {
    /**
     * Returns list containing all cart items packed in CartItemDTO
     *
     * @return List<CartItemDTO>
     */
    List<CartItemDTO> getCartItems();

    /**
     * Adds provided product with its count or update count of that product if it is already in cart.
     *
     * @param putDTO dto containing info about what to add or update in cart
     * @return cart item packed in CartItemDTO
     * @throws CartServiceValidationException when provided product with count are invalid
     */
    CartItemDTO addOrUpdateProductInCart(final CartPutDTO putDTO) throws CartServiceValidationException;

    /**
     * Removes product with provided UUID from cart and returns cart item containing info about deleted product.
     * If product was not in cart return CartItemDTO with 0 count for product.
     *
     * @param productId the UUID of product which will be deleted
     * @return deleted cart item packed in CartItemDTO
     */
    CartItemDTO deleteProductFromCart(final UUID productId);

    /**
     * Returns the email of user who owns the cart.
     * Can return null if cart is registered for anonymous user.
     *
     * @return email of user or null for anonymous
     */
    String getCartUserEmail();

    /**
     * Sets the email of user who owns the cart.
     * Should be used only if cart was for anonymous user to associate this cart with real user.
     *
     * @param email the email of new user owning the cart
     */
    void setCartUserEmail(final String email);

    /**
     * Clears cart and makes it empty
     */
    void clearCart();
}
