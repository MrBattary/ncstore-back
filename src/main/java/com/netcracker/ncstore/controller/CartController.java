package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.body.CartCheckoutBody;
import com.netcracker.ncstore.dto.request.CartDeleteRequest;
import com.netcracker.ncstore.dto.request.CartGetRequest;
import com.netcracker.ncstore.dto.body.CartPutBody;
import com.netcracker.ncstore.dto.request.CartCheckoutRequest;
import com.netcracker.ncstore.dto.request.CartPutRequest;
import com.netcracker.ncstore.dto.response.CartItemResponse;
import com.netcracker.ncstore.dto.response.OrderInfoResponse;
import com.netcracker.ncstore.service.cart.interfaces.ICartWebService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
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
    public ResponseEntity<List<CartItemResponse>> getShoppingCartProducts(final Locale locale,
                                                                          final Principal principal) {

        CartGetRequest request = new CartGetRequest(
                locale,
                principal.getName()
        );

        List<CartItemResponse> response = cartService.getCartItems(request);

        return ResponseEntity.
                ok().
                body(response);
    }

    @PutMapping
    public ResponseEntity<CartItemResponse> addProductToShoppingCart(@RequestBody final CartPutBody body,
                                                                     final Locale locale,
                                                                     final Principal principal) {

        CartPutRequest request = new CartPutRequest(
                body.getProductId(),
                body.getProductCount(),
                principal.getName(),
                locale
        );

        CartItemResponse response = cartService.putCartItem(request);

        return ResponseEntity.
                ok().
                body(response);
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable final UUID productId,
                                                   final Locale locale,
                                                   final Principal principal) {

        CartDeleteRequest request = new CartDeleteRequest(productId, principal.getName(), locale);

        CartItemResponse response = cartService.deleteCartItem(request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.
                    ok().
                    body(response);
        }

    }

    @PostMapping
    public ResponseEntity<OrderInfoResponse> checkout(@RequestBody CartCheckoutBody body,
                                                      final Locale locale,
                                                      final Principal principal) {
        CartCheckoutRequest request = new CartCheckoutRequest(
                body.isUseBalance(),
                body.getNonce(),
                principal.getName(),
                locale
        );

        OrderInfoResponse response = cartService.checkoutCartForUser(request);

        return ResponseEntity.ok().body(response);
    }
}
