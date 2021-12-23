package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PaymentProceedDTO {
    private final BigDecimal amount;
    private final String nonce;
    private final Locale region;
    private final UUID customerId;
}
