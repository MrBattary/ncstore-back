package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ReviewGetRequest {
    private int page;
    private final int size;
    private final UUID productId;
    private final String sort;
    private final String sortOrder;
}
