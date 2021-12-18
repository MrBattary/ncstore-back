package com.netcracker.ncstore.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.Locale;
import java.util.UUID;

/**
 * DTO containing information about product that will
 * be added (or updated, if exists in cart) to cart.
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class CartPutRequest {
    private final UUID productId;
    private final Integer productCount;
    private final String email;
    private final Locale locale;
}
