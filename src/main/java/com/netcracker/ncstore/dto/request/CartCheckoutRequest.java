package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

/**
 * DTO containing information about how to checkout user
 */
@AllArgsConstructor
@Getter
public class CartCheckoutRequest {
    private final boolean useBalance;
    private final String nonce;
    private final String email;
    private final Locale locale;
}
