package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AddBalanceDTO {
    private final String email;
    private final double amountToAdd;
}
