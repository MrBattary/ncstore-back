package com.netcracker.ncstore.service.cart.interfaces;

import com.netcracker.ncstore.dto.CartItemDTO;
import com.netcracker.ncstore.dto.CartPutDTO;

import java.util.List;
import java.util.UUID;

public interface ICartBusinessService {
    List<CartItemDTO> getCartItems();
    CartItemDTO addOrUpdateProductInCart(CartPutDTO putDTO);
    CartItemDTO deleteProductFromCart(UUID productId);
    String getCartUserEmail();
    void setCartUserEmail(String email);
}
