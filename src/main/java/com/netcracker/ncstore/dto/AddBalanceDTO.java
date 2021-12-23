package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;

@AllArgsConstructor
@Getter
public class AddBalanceDTO {
    private final String email;
    private final double amountToAddInRealMoney;
    private final double amountToAddInUC;
    private final String paymentNonce;
    private final Locale locale;
}
