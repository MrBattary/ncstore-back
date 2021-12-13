package com.netcracker.ncstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderItemGetDTO {
    private final UUID orderItemId;
    private final String email;
}
