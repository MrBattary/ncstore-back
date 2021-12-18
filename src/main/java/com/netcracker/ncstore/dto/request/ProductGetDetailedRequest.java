package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProductGetDetailedRequest {
    private final UUID productId;
    private final String emailOfIssuer;
}
