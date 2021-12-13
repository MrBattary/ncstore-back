package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CartItemDTO {
    private final UUID productId;
    private final int countOfProduct;
}
