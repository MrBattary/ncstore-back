package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.UUID;

/**
 * DTO containing information about order payment
 */
@AllArgsConstructor
@Getter
public class OrderPayDTO {
    private final UUID orderId;
    private final String email;
    private final Locale region;
    private final boolean useBalance;
    private final String nonce;
}
