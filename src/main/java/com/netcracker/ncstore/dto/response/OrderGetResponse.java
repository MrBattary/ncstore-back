package com.netcracker.ncstore.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.netcracker.ncstore.model.enumerations.EOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;
import java.util.UUID;

/**
 * Response giving minimal info about orders. Used as response for pagination
 */
@Jacksonized
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Getter
public class OrderGetResponse {
    private final UUID orderId;
    private final EOrderStatus status;
    private final Instant timeStampUtc;
}
