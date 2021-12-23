package com.netcracker.ncstore.dto.body;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

/**
 * DTO containing request body for review POST request
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class ReviewCreateBody {
    private final UUID productId;
    private final int rating;
    private final String reviewText;
}
