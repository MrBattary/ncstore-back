package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserBalanceDTO {
    private final String email;
    private final double balance;
    private final String currencySymbol;
}
