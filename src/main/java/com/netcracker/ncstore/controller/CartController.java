package com.netcracker.ncstore.controller;

import com.netcracker.ncstore.dto.ActualProductPriceWithCurrencySymbolDTO;
import com.netcracker.ncstore.dto.ProductLocaleDTO;
import com.netcracker.ncstore.dto.request.CartAddOrUpdateRequest;
import com.netcracker.ncstore.dto.response.CartAddOrUpdateResponse;
import com.netcracker.ncstore.service.cart.ICartService;
import com.netcracker.ncstore.service.price.IPricesService;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/cart")
public class CartController {
    private final ICartService cartService;
    private final IPricesService pricesService;

    public CartController(final ICartService cartService,
                          final IPricesService pricesService) {
        this.cartService = cartService;
        this.pricesService = pricesService;
    }

    @GetMapping
    public ResponseEntity<List<CartAddOrUpdateResponse>> getShoppingCartProducts(final Locale locale) {
        Map<UUID, Integer> cartItems = cartService.getCartItems();


        List<CartAddOrUpdateResponse> responses = new ArrayList<>();

        responses = cartItems.entrySet().
                stream().
                map(e -> createResponseDTO(e.getKey(), e.getValue(), locale)).
                sorted(Comparator.comparing(CartAddOrUpdateResponse::getProductName)).
                collect(Collectors.toList());

        return ResponseEntity.ok().body(responses);
    }

    @PutMapping
    public ResponseEntity<CartAddOrUpdateResponse> addProductToShoppingCart(@RequestBody final CartAddOrUpdateRequest request,
                                                                            final Locale locale) {

        cartService.addOrUpdateProduct(request.getProductId(), request.getProductCount());

        CartAddOrUpdateResponse response = createResponseDTO(
                request.getProductId(),
                request.getProductCount(),
                locale);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<?> checkout() {

        return null;
    }

    @DeleteMapping(value = "/{productId}")
    public ResponseEntity<?> deleteProductFromCart(@PathVariable final UUID productId,
                                                   final Locale locale) {
        Integer count = cartService.deleteProduct(productId);
        if (count == 0) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.
                    ok().
                    body(createResponseDTO(productId, count, locale));
        }

    }

    /**
     * Private function used to prevent code duplication and to get response DTO
     */
    private CartAddOrUpdateResponse createResponseDTO(UUID productId, Integer productCount, Locale locale) {
        ActualProductPriceWithCurrencySymbolDTO priceForProduct =
                pricesService.getActualPriceForProductInRegion(new ProductLocaleDTO(productId, locale));

        return new CartAddOrUpdateResponse(
                productId,
                productCount,
                priceForProduct.getProductName(),
                priceForProduct.getNormalPrice(),
                priceForProduct.getDiscountPrice(),
                priceForProduct.getPriceCurrency());
    }
}
