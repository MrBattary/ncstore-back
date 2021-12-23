package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Locale;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class CartDeleteRequest {
    private final UUID productId;
    private final String email;
    private final Locale locale;
}
