package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductGetStatisticsRequest {
    private final String emailOfIssuer;
    private final UUID productId;
}
