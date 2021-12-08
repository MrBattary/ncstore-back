package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CheckoutDetails {
    private final Map<UUID, Integer> productsToBuyWithCount;
    private final UUID userId;
    private final Locale region;
    private final boolean useBalance;
    private final String nonce;
}
