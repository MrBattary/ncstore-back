package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.UUID;

/**
 * DTO containing information about product that will
 * be added (or updated, if exists in cart) to cart.
 */
@AllArgsConstructor
@Getter
public class CartPutRequest {
    private final UUID productId;
    private final Integer productCount;
    private final String email;
    private final Locale locale;
}
