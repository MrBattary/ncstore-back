package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CartPutDTO {
    private final UUID ProductId;
    private final int countOfProduct;
}
