package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.UUID;

@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class ReviewGetResponse {
    private final UUID reviewId;
    private final UUID productId;
    private final UUID authorId;
    private final String authorName;
    private final Instant creationTimeUtc;
    private final int productRating;
    private final String reviewText;
}
