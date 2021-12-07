package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public class CartCheckoutDTO {
    private final boolean useBalance;
    private final String nonce;
    private final Locale locale;
}
