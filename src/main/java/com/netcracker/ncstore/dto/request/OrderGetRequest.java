package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This DTO contains all GET request
 * information for getting order with pagination
 */
@AllArgsConstructor
@Getter
public class OrderGetRequest {
    private final int page;
    private final int size;
    private final String email;
}
