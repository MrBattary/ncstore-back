package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PaymentProceedDTO {
    private final BigDecimal amount;
    private final String nonce;
    private final String userName;
}
