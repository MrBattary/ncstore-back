package com.netcracker.ncstore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class ReviewCreateRequest {
    private final String emailOfAuthor;
    private final UUID productId;
    private final int rating;
    private final String text;
}
