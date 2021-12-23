package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

/**
 * DTO containing info for /user/balance POST request
 */
@AllArgsConstructor
@Getter
public class UserAddBalanceRequest {
    private final double paymentAmount;
    private final String nonce;
    private final String email;
    private final Locale locale;
}
