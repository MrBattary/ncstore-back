package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class OrderInfoGetRequest {
    private final UUID orderId;
    private final String email;
}
