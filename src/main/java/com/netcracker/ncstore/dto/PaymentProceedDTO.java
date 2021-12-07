package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Locale;

@Getter
@AllArgsConstructor
public class PaymentProceedDTO {
    private final BigDecimal amount;
    private final String nonce;
    private final Locale region;
}
