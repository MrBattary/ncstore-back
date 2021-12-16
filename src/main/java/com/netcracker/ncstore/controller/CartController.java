package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.CartCheckoutBody;
import com.netcracker.ncstore.dto.body.CartPutBody;
import com.netcracker.ncstore.dto.request.CartCheckoutRequest;
import com.netcracker.ncstore.dto.request.CartDeleteRequest;
import com.netcracker.ncstore.dto.request.CartGetRequest;
import com.netcracker.ncstore.dto.request.CartPutRequest;
import com.netcracker.ncstore.dto.response.CartItemResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.service.cart.interfaces.ICartWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/cart")
public class CartController {
    private final ICartWebService cartService;

    public CartController(final ICartWebService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getShoppingCartProducts(final Locale locale) {

        CartGetRequest request = new CartGetRequest(
                locale,
                SecurityContextHolder.getContext().getAuthentication().getName()
        );

        List<CartItemResponse> response = cartService.getCartItems(request);

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @PutMapping
    public ResponseEntity<CartItemResponse> addProductToShoppingCart(@RequestBody final CartPutBody body,
                                                                     final Locale locale) {

        CartPutRequest request = new CartPutRequest(
                body.getProductId(),
                body.getProductCount(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                locale
        );

        CartItemResponse response = cartService.putCartItem(request);

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable final UUID productId,
                                                   final Locale locale) {

        CartDeleteRequest request = new CartDeleteRequest(
                productId,
                SecurityContextHolder.getContext().getAuthentication().getName(),
                locale
        );

        CartItemResponse response = cartService.deleteCartItem(request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.
                    ok().
                    contentType(MediaType.APPLICATION_JSON).
                    body(response);
        }
    }

    @PostMapping
    public ResponseEntity<OrderInfoResponse> checkout(@RequestBody CartCheckoutBody body,
                                                      final Locale locale) {
        CartCheckoutRequest request = new CartCheckoutRequest(
                body.isUseBalance(),
                body.getNonce(),
                SecurityContextHolder.getContext().getAuthentication().getName(),
                locale
        );

        OrderInfoResponse response = cartService.checkoutCartForUser(request);

        return ResponseEntity.
                ok().
                contentType(MediaType.APPLICATION_JSON).
                body(response);
    }
}
